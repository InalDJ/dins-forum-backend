package com.java.springportfolio.service;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);

    void updateComment(CommentRequest commentRequest);

    void deleteComment(Long commentId);

    List<CommentResponse> getAllComments(String orderType, int pageNumber, int commentsQuantity);

    List<CommentResponse> getCommentsByPostAndParentComment(Long postId, Long parentCommentId);

    List<CommentResponse> getAllCommentsByPost(Long postId);

    CommentResponse getComment(Long commentId);
}
