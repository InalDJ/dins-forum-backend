package com.java.springportfolio.service;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    void createComment(CommentRequest commentRequest);

    void updateComment(CommentRequest commentRequest);

    void deleteComment(Long commentId);

    List<CommentResponse> getAllCommentsSortedByCreationDate();

    CommentResponse getComment(Long commentId);
}
