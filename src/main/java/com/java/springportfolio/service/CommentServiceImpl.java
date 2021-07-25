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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    @Override
    public void createComment(CommentRequest commentRequest) {
        log.info("Creating a comment...");
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ItemNotFoundException("Post with id: " + commentRequest.getPostId() + " has not been found"));
        User currentUser = authService.getCurrentUser();
        commentRepository.save(commentMapper.mapToCreateNewComment(commentRequest, currentUser, post));
        log.info("The comment has been saved to the database");
    }

    @Override
    public void updateComment(CommentRequest commentRequest) {
        log.info("Updating comment with id: {}'", commentRequest.getId());
        if (commentRequest.getId() == null) {
            log.error("Comment id is null. The comment cannot be updated!");
            throw new PortfolioException("Comment id is null!");
        }
        Comment comment = commentRepository.findById(commentRequest.getId())
                .orElseThrow(() -> new ItemNotFoundException("Comment with id: " + commentRequest.getId() + " has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (comment.getUser().getUserId() != currentUser.getUserId()) {
            log.error("Only comment authors can update comments!");
            throw new PortfolioException("Only comment authors can update comments!");
        }
        commentRepository.save(commentMapper.mapToUpdateExistingComment(commentRequest, comment));
        log.info("Comment with id: '{}' has been saved to the database!", commentRequest.getId());
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: '{}'...", commentId);
        boolean commentExists = commentRepository.existsById(commentId);
        if (!commentExists) {
            throw new ItemNotFoundException("The comment does not exist!");
        }
        commentRepository.deleteById(commentId);
        log.info("Comment with id: '{}' has been deleted!", commentId);
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
