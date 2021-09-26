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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.java.springportfolio.service.OrderType.NEW;
import static com.java.springportfolio.service.OrderType.POPULAR;
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
        log.info("Creating a comment...");
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
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new ItemNotFoundException("The comment does not exist!"));
        if (comment.getParentCommentId() != null && comment.getParentCommentId() != 0) {
            log.info("Comment has a parent comment. Decrementing subCommentCount");
            commentRepository.decrementSubcommentCount(comment.getParentCommentId());
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
    public List<CommentResponse> getAllComments(String orderType, int pageNumber, int commentsQuantity) {
        return getPostsSortedByOrderType(orderType, pageNumber, commentsQuantity);
    }

    @Override
    public List<CommentResponse> getAllCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findAllCommentsByPost(postId).orElseThrow(() -> new ItemNotFoundException("The comment list is null!"));
        return comments.stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponse> getCommentsByPostAndParentComment(Long postId, Long parentCommentId) {
        log.info("Parentcommentid = " + parentCommentId);
        List<Comment> comments = commentRepository.findAllCommentsByPostAndParentCommentId(postId, parentCommentId).orElseThrow(() -> new ItemNotFoundException("The comment list is null!"));
        return comments.stream().map(commentMapper::mapToDto).collect(Collectors.toList());
    }

    private List<CommentResponse> getPostsSortedByOrderType(String orderType, int pageNumber, int commentQuantity) {
        if (orderType == null) {
            throw new PortfolioException("Order type is null!");
        }
        Pageable pageable = getPageable(pageNumber, commentQuantity);
        OrderType existingOrderType = findOrderType(orderType);
        if (existingOrderType.equals(NEW)) {
            List<Comment> comments = commentRepository.findAllCommentsOrderByCreatedDate(pageable)
                    .orElseThrow(() -> new ItemNotFoundException("There are no existing comments!"));
            return comments.stream().map(commentMapper::mapToDto).collect(Collectors.toList());
        }
        if (existingOrderType.equals(POPULAR)) {
            List<Comment> comments = commentRepository.findAllCommentsOrderByNumberOfVotes(pageable)
                    .orElseThrow(() -> new ItemNotFoundException("There are no existing comments!"));
            return comments.stream().map(commentMapper::mapToDto).collect(Collectors.toList());
        }
        throw new ItemNotFoundException("There are no comments!");
    }

    private Pageable getPageable(int pageNumber, int postsPerPage) {
        return PageRequest.of(pageNumber, postsPerPage);
    }
}
