package com.java.springportfolio.service;

import com.java.springportfolio.dao.CommentRepository;
import com.java.springportfolio.dao.PostRepository;
import com.java.springportfolio.dto.CommentPayload;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.java.springportfolio.factory.PageableFactory.*;
import static com.java.springportfolio.service.OrderType.findOrderType;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;

    @Transactional
    @Override
    public void createComment(CommentRequest commentRequest) {
        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ItemNotFoundException("Post with id: " + commentRequest.getPostId() + " has not been found"));
        User currentUser = authService.getCurrentUser();
        if (commentRequest.getParentCommentId() != null) {
            boolean parentCommentExists = commentRepository.existsById(commentRequest.getParentCommentId());
            if (!parentCommentExists) {
                log.info("Parent comment with id - {} has not been found!", commentRequest.getParentCommentId());
                commentRequest.setParentCommentId(null);
            } else {
                commentRepository.incrementSubcommentCount(commentRequest.getParentCommentId());
            }
        }
        commentRepository.save(commentMapper.mapToCreateNewComment(commentRequest, currentUser, post));
    }

    @Override
    public void updateComment(CommentRequest commentRequest) {
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
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        log.info("Deleting comment with id: '{}'...", commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("The comment does not exist!"));
        if (comment.getParentCommentId() != null && comment.getParentCommentId() != 0) {
            log.info("Comment has a parent comment. Decrementing subCommentCount");
            commentRepository.decrementSubcommentCount(comment.getParentCommentId());
        }
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentPayload getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ItemNotFoundException("The comment has not been found!"));
        return commentMapper.mapToCommentPayloadDto(comment);
    }

    @Override
    public CommentResponse getAllComments(String orderType, int pageNumber, int commentsQuantity) {
        return getCommentsSortedByOrderType(orderType, pageNumber, commentsQuantity);
    }

    @Override
    public CommentResponse getAllCommentsByPost(Long postId, int pageNumber, int commentQuantity) {
        Pageable pageable = getPageableWithSortingByDateAndVoteCount(pageNumber, commentQuantity);
        Page<Comment> commentPage = commentRepository.findAllCommentsByPost(postId, pageable).orElseThrow(() -> new ItemNotFoundException("The comment list is null!"));
        return commentMapper.mapToCommentResponse(commentPage);
    }

    @Override
    public CommentResponse getAllCommentsByUser(String userName, int pageNumber, int commentQuantity) {
        Pageable pageable = getPageableWithSortingByDate(pageNumber, commentQuantity);
        Page<Comment> commentPage = commentRepository.findAllCommentsByUser(userName, pageable).orElse(Page.empty());
        return commentMapper.mapToCommentResponse(commentPage);
    }

    @Override
    public List<CommentPayload> getCommentsByPostAndParentComment(Long parentCommentId) {
        log.info("Parentcommentid = " + parentCommentId);
        List<Comment> comments = commentRepository.findAllCommentsByPostAndParentCommentId(parentCommentId).orElseThrow(() -> new ItemNotFoundException("The comment list is null!"));
        return comments.stream().map(commentMapper::mapToCommentPayloadDto).collect(Collectors.toList());
    }

    private CommentResponse getCommentsSortedByOrderType(String orderType, int pageNumber, int commentQuantity) {
        if (orderType == null) {
            throw new PortfolioException("Order type is null!");
        }
        OrderType existingOrderType = findOrderType(orderType);
        Pageable pageable = getPageableByOrderType(existingOrderType, pageNumber, commentQuantity);
        Page<Comment> commentPage = commentRepository.findAllComments(pageable)
                .orElseThrow(() -> new ItemNotFoundException("There are no existing comments!"));
        return commentMapper.mapToCommentResponse(commentPage);
    }
}
