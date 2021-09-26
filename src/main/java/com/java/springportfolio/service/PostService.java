package com.java.springportfolio.service;

import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;

import java.util.List;

public interface PostService {

    void createPost(PostRequest postRequest);

    void updatePost(PostRequest postRequest);

    PostResponse getPost(Long postId);

    List<PostResponse> getAllPosts(String orderType, int pageNumber, int postsPerPage);

    List<PostResponse> getAllPostsByTopic(String topic);

    void deletePost(Long postId);
}
