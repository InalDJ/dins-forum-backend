package com.java.springportfolio.service;

import com.java.springportfolio.dto.CommentPayload;
import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);

    void updateComment(CommentRequest commentRequest);

    void deleteComment(Long commentId);

    CommentResponse getAllComments(String orderType, int pageNumber, int commentsQuantity);

    List<CommentPayload> getCommentsByPostAndParentComment(Long postId, Long parentCommentId);

    CommentResponse getAllCommentsByPost(Long postId, int pageNumber, int commentsQuantity);

    CommentPayload getComment(Long commentId);
}
