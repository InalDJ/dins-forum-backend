package com.java.springportfolio.service;

import com.java.springportfolio.dao.PostRepository;
import com.java.springportfolio.dao.TopicRepository;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.Topic;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.exception.ItemAlreadyExistsException;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.java.springportfolio.service.OrderType.NEW;
import static com.java.springportfolio.service.OrderType.POPULAR;
import static com.java.springportfolio.service.OrderType.findOrderType;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final TopicRepository topicRepository;
    private final PostMapper postMapper;
    private final AuthService authService;

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
        postRepository.save(postMapper.mapToCreateNewPost(postRequest, currentUser, topic));
        log.info("The post with name: '{}' has been saved to the database!", postRequest.getPostName());
    }

    @Override
    public void updatePost(PostRequest postRequest) {
        log.info("Updating post...");
        Post existingPostById = postRepository.findById(postRequest.getPostId())
                .orElseThrow(() -> new ItemNotFoundException("Post has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (currentUser.getUserId() != existingPostById.getUser().getUserId()) {
            log.error("Only post creator can edit posts!");
            throw new PortfolioException("Only post creator can edit posts!");
        }
        Post existingPostByName = postRepository.findByPostName(postRequest.getPostName()).orElse(null);
        if (existingPostByName != null && existingPostByName.getPostId() != existingPostById.getPostId()) {
            log.error("The Post with name: '{}' already exists!", postRequest.getPostName());
            throw new ItemAlreadyExistsException("The Post with name: '" + postRequest.getPostName() + "' already exists!");
        }
        postRepository.save(postMapper.mapToUpdateExistingPost(postRequest, existingPostById));
        log.info("The post with id: '{}' has been successfully updated!", postRequest.getPostId());
    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ItemNotFoundException("Post has not been found!"));
        return postMapper.mapToDto(post);
    }

    @Override
    public List<PostResponse> getAllPosts(String orderType, int pageNumber, int postsPerPage) {
        return getPostsSortedByOrderType(orderType, pageNumber, postsPerPage);
    }

    @Override
    public List<PostResponse> getAllPostsByTopic(String topic) {
        List<Post> posts = postRepository.findAllPostsByTopic(topic)
                .orElseThrow(() -> new ItemNotFoundException("There are no existing posts!"));
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public void deletePost(Long postId) {
        log.info("Deleting post with id: '{}'...", postId);
        if (!postRepository.existsById(postId)) {
            log.error("Post with id: '{}' does not exist!", postId);
            throw new ItemNotFoundException("Post doesn't exist!");
        }
        postRepository.deleteById(postId);
        log.info("Post with id: '{}' hs been deleted!", postId);
    }

    private List<PostResponse> getPostsSortedByOrderType(String orderType, int pageNumber, int postsPerPage) {
        if (orderType == null) {
            throw new PortfolioException("Order type is null!");
        }
        Pageable pageable = getPageable(pageNumber, postsPerPage);
        OrderType existingOrderType = findOrderType(orderType);
        if (existingOrderType.equals(NEW)) {
            List<Post> posts = postRepository.findAllPostsOrderByCreatedDate(pageable)
                    .orElseThrow(() -> new ItemNotFoundException("There are no existing posts!"));
            return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
        }
        if (existingOrderType.equals(POPULAR)) {
            List<Post> posts = postRepository.findAllPostsOrderByNumberOfVotes(pageable)
                    .orElseThrow(() -> new ItemNotFoundException("There are no existing posts!"));
            return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());
        }
        throw new ItemNotFoundException("There are no posts!");
    }

    private Pageable getPageable(int pageNumber, int postsPerPage) {
        return PageRequest.of(pageNumber, postsPerPage);
    }
}
