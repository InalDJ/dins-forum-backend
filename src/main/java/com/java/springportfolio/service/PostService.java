package com.java.springportfolio.service;

import com.java.springportfolio.dto.PostPayload;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;

public interface PostService {

    void createPost(PostRequest postRequest);

    void updatePost(PostRequest postRequest);

    PostPayload getPost(Long postId);

    PostResponse getAllPosts(String orderType, int pageNumber, int postsPerPage);

    PostResponse getAllPostsByTopic(String topicName, String orderType, int pageNumber, int postsPerPage);

    void deletePost(Long postId);

    PostResponse getAllPostsByUser(String userName, int pageNumber, int postsPerPage);
}
