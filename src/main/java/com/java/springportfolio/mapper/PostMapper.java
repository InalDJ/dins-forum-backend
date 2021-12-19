package com.java.springportfolio.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.java.springportfolio.dto.FilePayload;
import com.java.springportfolio.dto.PostPayload;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.FileRecord;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.Topic;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.service.PostVoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.java.springportfolio.entity.VoteType.DOWNVOTE;
import static com.java.springportfolio.entity.VoteType.UPVOTE;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final PostVoteService postVoteService;

    public PostResponse mapToPostResponse(Page<Post> postPage) {
        List<PostPayload> postPayload = postPage.getContent().stream().map(this::mapPostEntityToPostPayload).collect(Collectors.toList());
        return PostResponse.builder()
                .posts(postPayload)
                .totalPages(postPage.getTotalPages())
                .numberOfElementsPerPage(postPage.getNumberOfElements())
                .numberOfElementsTotal(postPage.getTotalElements())
                .pageNumber(postPage.getNumber())
                .build();
    }

    public PostPayload mapPostEntityToPostPayload(Post postEntity) {
        return PostPayload.builder()
                .id(postEntity.getPostId())
                .postName(postEntity.getPostName())
                .topicName(postEntity.getTopic().getName())
                .voteCount(postEntity.getVoteCount())
                .description(postEntity.getDescription())
                .duration(getDuration(postEntity))
                .createdDate(postEntity.getCreatedDate().toString())
                .upVote(isPostUpVoted(postEntity))
                .downVote(isPostDownVoted(postEntity))
                .commentCount(getCommentCount(postEntity))
                .userName(postEntity.getUser().getUsername())
                .files(mapFileRecordEntityListToFilePayloadList(postEntity.getFileRecords()))
                .build();
    }

    public Post mapPostRequestToPostEntity(PostRequest postRequest, User user, Topic topic) {
        return Post.builder()
                .postName(postRequest.getPostName())
                .description(postRequest.getDescription())
                .createdDate(Instant.now())
                .user(user)
                .voteCount(0)
                .topic(topic)
                .build();
    }

    public Post updatePostEntityFields(PostRequest postRequest, Post existingPostEntity, List<FileRecord> fileRecordEntityList) {
        existingPostEntity.setPostName(postRequest.getPostName());
        existingPostEntity.setDescription(postRequest.getDescription());
        existingPostEntity.setFileRecords(fileRecordEntityList);
        return existingPostEntity;
    }

    public List<FileRecord> mapFilesPayloadToFileRecordEntityList(List<FilePayload> filesPayload, User user) {
        return mapFilesPayloadToFileRecordEntityList(filesPayload, user, null);
    }

    public List<FileRecord> mapFilesPayloadToFileRecordEntityList(List<FilePayload> filesPayload, User user, Comment comment) {
        if (filesPayload == null || filesPayload.isEmpty()) {
            return null;
        }
        List<FileRecord> fileRecordEntityList = new ArrayList<>();
        for (FilePayload filePayload : filesPayload) {
            FileRecord fileRecordEntity = FileRecord.builder()
                    .url(filePayload.getUrl())
                    .path(filePayload.getPath())
                    .createdDate(Instant.now())
                    //.post(post)
                    .userId(user.getUserId())
                    .comment(comment)
                    .build();
            fileRecordEntityList.add(fileRecordEntity);
        }
        return fileRecordEntityList;
    }

    public List<FilePayload> mapFileRecordEntityListToFilePayloadList(List<FileRecord> fileRecordEntityList) {
        if (fileRecordEntityList == null || fileRecordEntityList.isEmpty()) {
            return null;
        }
        List<FilePayload> filePayloadList = new ArrayList<>();
        for (FileRecord fileRecord : fileRecordEntityList) {
            FilePayload filePayload = FilePayload.builder()
                    .id(fileRecord.getRecordId())
                    .url(fileRecord.getUrl())
                    .path(fileRecord.getPath())
                    .postId(fileRecord.getPost() != null ? fileRecord.getPost().getPostId() : null)
                    .commentId(fileRecord.getComment() != null ? fileRecord.getComment().getId() : null)
                    .build();
            filePayloadList.add(filePayload);
        }
        return filePayloadList;
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return postVoteService.checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return postVoteService.checkVoteType(post, DOWNVOTE);
    }

    Integer getCommentCount(Post post) {
        return post.getComments() != null ? post.getComments().size() : 0;
    }
}
