package com.java.springportfolio.service;

import com.java.springportfolio.dao.PostRepository;
import com.java.springportfolio.dao.TopicRepository;
import com.java.springportfolio.dto.FilePayload;
import com.java.springportfolio.dto.PostPayload;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.entity.*;
import com.java.springportfolio.exception.ItemAlreadyExistsException;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.java.springportfolio.factory.PageableFactory.*;
import static com.java.springportfolio.service.OrderType.findOrderType;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final MediaService mediaService;

    @Override
    public void createPost(PostRequest postRequest) {
        log.info("Creating a post...");
        if (postRepository.existsByPostName(postRequest.getPostName())) {
            log.error("Post with name: '{}' already exists!", postRequest.getPostName());
            throw new ItemAlreadyExistsException("Post with the name: " + postRequest.getPostName() + " already exists!");
        }
        Topic topic = topicRepository.findById(postRequest.getTopicId())
                .orElseThrow(() -> new ItemNotFoundException("Topic has not been found!"));
        User currentUser = authService.getCurrentUser();
        Post postSaved = postRepository.save(postMapper.mapPostRequestToPostEntity(postRequest, currentUser, topic));
        List<FileRecord> fileRecordEntityListToSave = postMapper.mapFilesPayloadToFileRecordEntityList(postRequest.getFiles(), currentUser, postSaved);
        List<FileRecord> fileRecordsSaved = mediaService.saveFileRecordsToDatabase(fileRecordEntityListToSave);
        postSaved.setFileRecords(fileRecordsSaved);
        postRepository.save(postSaved);
        log.info("The post with name: '{}' has been saved to the database!", postRequest.getPostName());
    }

    @Override
    public void updatePost(PostRequest postRequest) {
        Post existingPostById = postRepository.findById(postRequest.getPostId())
                .orElseThrow(() -> new ItemNotFoundException("Post has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (!currentUser.getUserId().equals(existingPostById.getUser().getUserId())) {
            log.error("Only post creator can edit posts!");
            throw new PortfolioException("Only post creator can edit posts!");
        }
        Post existingPostByName = postRepository.findByPostName(postRequest.getPostName()).orElse(null);
        if (existingPostByName != null && existingPostByName.getPostId() != existingPostById.getPostId()) {
            log.error("The Post with name: '{}' already exists!", postRequest.getPostName());
            throw new ItemAlreadyExistsException("The Post with name: '" + postRequest.getPostName() + "' already exists!");
        }
        List<FileRecord> updatedFileRecordList = getUpdatedFileRecordList(postRequest, currentUser, existingPostById);
        postRepository.save(postMapper.updatePostEntityFields(postRequest, existingPostById, updatedFileRecordList));
        log.info("The post with id: '{}' has been successfully updated!", postRequest.getPostId());
    }

    private List<FileRecord> getUpdatedFileRecordList(PostRequest postRequest, User user, Post existingPost) {
        List<FileRecord> existingFileRecordEntityList = existingPost.getFileRecords();
        List<FilePayload> filePayloadList = postRequest.getFiles();
        if ((filePayloadList == null || filePayloadList.isEmpty()) && existingFileRecordEntityList == null) {
            return null;
        }
        if ((filePayloadList == null || filePayloadList.isEmpty()) && (existingFileRecordEntityList != null || !existingFileRecordEntityList.isEmpty())) {
            mediaService.deleteFileRecordsFromDatabase(existingFileRecordEntityList);
            return null;
        }
        if ((filePayloadList != null && !filePayloadList.isEmpty()) && (existingFileRecordEntityList == null || existingFileRecordEntityList.isEmpty())) {
            List<FileRecord> updatedFileRecordList = postMapper.mapFilesPayloadToFileRecordEntityList(filePayloadList, user, existingPost);
            mediaService.saveFileRecordsToDatabase(updatedFileRecordList);
            return updatedFileRecordList;
        }
        List<Long> fileRecordIdList = new ArrayList<>();
        List<Long> filePayloadIdList = new ArrayList<>();
        for (FileRecord fileRecord : existingFileRecordEntityList) {
            fileRecordIdList.add(fileRecord.getRecordId() != null ? fileRecord.getRecordId() : -1);
        }
        for (FilePayload filePayload : filePayloadList) {
            filePayloadIdList.add(filePayload.getId() != null ? filePayload.getId() : -1);
        }
        Collections.sort(fileRecordIdList);
        Collections.sort(filePayloadIdList);
        if (fileRecordIdList.equals(filePayloadIdList)) {
            return existingFileRecordEntityList;
        }
        mediaService.deleteFileRecordsFromDatabase(existingFileRecordEntityList);
        return mediaService.saveFileRecordsToDatabase(
                postMapper.mapFilesPayloadToFileRecordEntityList(filePayloadList, user, existingPost));
    }

    @Override
    public PostPayload getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ItemNotFoundException("Post has not been found!"));
        return postMapper.mapPostEntityToPostPayload(post);
    }

    @Override
    public PostResponse getAllPosts(String orderType, int pageNumber, int postsPerPage) {
        return getPostsSortedByOrderType(orderType, pageNumber, postsPerPage);
    }

    @Override
    public PostResponse getAllPostsByTopic(String topicName, String orderType, int pageNumber, int postsPerPage) {
        OrderType existingOrderType = findOrderType(orderType);
        Pageable pageable = getPageableByOrderType(existingOrderType, pageNumber, postsPerPage);
        Page<Post> postPage = postRepository.findAllPostsByTopic(topicName, pageable)
                .orElseThrow(() -> new ItemNotFoundException("There are no existing posts!"));
        return postMapper.mapToPostResponse(postPage);
    }

    @Override
    public PostResponse getAllPostsByUser(String userName, int pageNumber, int postsPerPage) {
        User currentUser = authService.getCurrentUser();
        if (!currentUser.getUsername().equalsIgnoreCase(userName)) {
            throw new PortfolioException("Only authorized users can access the posts!");
        }
        Pageable pageable = getPageableWithSortingByDate(pageNumber, postsPerPage);
        Page<Post> postPage = postRepository.findAllPostsByUser(currentUser.getUserId(), pageable)
                .orElse(Page.empty());
        return postMapper.mapToPostResponse(postPage);
    }

    @Override
    public void deletePost(Long postId) {
        log.info("Deleting post with id: '{}'...", postId);
        if (!postRepository.existsById(postId)) {
            log.error("Post with id: '{}' does not exist!", postId);
            throw new ItemNotFoundException("Post doesn't exist!");
        }
        postRepository.deleteById(postId);
        log.info("Post with id: '{}' has been deleted!", postId);
    }

    private PostResponse getPostsSortedByOrderType(String orderType, int pageNumber, int postsPerPage) {
        if (orderType == null) {
            throw new PortfolioException("Order type is null!");
        }
        OrderType existingOrderType = findOrderType(orderType);
        Pageable pageable = getPageableByOrderType(existingOrderType, pageNumber, postsPerPage);
        Page<Post> postPage = postRepository.findAllPosts(pageable)
                .orElseThrow(() -> new ItemNotFoundException("There are no existing posts!"));
        return postMapper.mapToPostResponse(postPage);
    }
}
