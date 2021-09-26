package com.java.springportfolio.controller;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.service.CommentService;
import com.java.springportfolio.util.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final DtoValidator dtoValidator;

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getAllComments(@RequestParam String orderType,
                                                                @RequestParam int pageNumber,
                                                                @RequestParam int commentsQuantity) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments(orderType, pageNumber, commentsQuantity));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> getAllCommentsByPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByPost(postId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(commentId));
    }

    @GetMapping("/post/{postId}/comment/{parentCommentId}")
    public ResponseEntity<List<CommentResponse>> getSubCommentsByPostAndParentComment(@PathVariable Long postId, @PathVariable Long parentCommentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByPostAndParentComment(postId, parentCommentId));
    }

    @PostMapping
    public ResponseEntity<String> createComment(@RequestBody CommentRequest commentRequest) {
        dtoValidator.validateCommentRequest(commentRequest);
        commentService.createComment(commentRequest);
        return new ResponseEntity<>("A new comment has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateComment(@RequestBody CommentRequest commentRequest) {
        dtoValidator.validateCommentRequest(commentRequest);
        commentService.updateComment(commentRequest);
        return new ResponseEntity<>("The comment has been updated!", HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("The comment has been successfully deleted!", HttpStatus.OK);
    }
}
