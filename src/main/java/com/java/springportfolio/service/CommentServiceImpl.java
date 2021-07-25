package com.java.springportfolio.service;

import com.java.springportfolio.dao.CommentRepository;
import com.java.springportfolio.dao.PostRepository;
import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, AuthService authService, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authService = authService;
        this.commentMapper = commentMapper;
    }

    @Override
    public void createComment(CommentRequest commentRequest) {
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ItemNotFoundException("Post with id: " + commentRequest.getPostId() + " has not been found"));
        User currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.mapToCreateNewComment(commentRequest, currentUser, post));
    }

    @Override
    public void updateComment(CommentRequest commentRequest) {
        if (commentRequest.getId() == null) {
            throw new PortfolioException("Comment id is null!");
        }
        Comment comment = commentRepository.findById(commentRequest.getId())
                .orElseThrow(() -> new ItemNotFoundException("Comment with id: " + commentRequest.getId() + " has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (comment.getUser().getUserId() != currentUser.getUserId()) {
            throw new PortfolioException("Only comment writers can update comments!");
        }
        commentRepository.save(commentMapper.mapToUpdateExistingComment(commentRequest, comment));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        boolean commentExists = commentRepository.existsById(commentId);
        if (!commentExists) {
            throw new ItemNotFoundException("The comment does not exist!");
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ItemNotFoundException("The comment has not been found!"));
        return commentMapper.mapToDto(comment);
    }

    @Override
    public List<CommentResponse> getAllCommentsSortedByCreationDate() {
        List<Comment> comments = commentRepository.findAllCommentsOrderByCreatedDate()
                .orElseThrow(() -> new ItemNotFoundException("The comment list is null!"));
        return comments.stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }
}
