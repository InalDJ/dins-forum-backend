package com.java.springportfolio.controller;

import com.java.springportfolio.dto.CommentPayload;
import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.service.CommentService;
import com.java.springportfolio.util.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final DtoValidator dtoValidator;

    @GetMapping
    public ResponseEntity<CommentResponse> getAllComments(@RequestParam String orderType,
                                                          @RequestParam int pageNumber,
                                                          @RequestParam int commentsQuantity) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllComments(orderType, pageNumber, commentsQuantity));
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommentResponse> getAllCommentsByPost(@PathVariable Long postId,
                                                                @RequestParam int pageNumber,
                                                                @RequestParam int commentQuantity) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByPost(postId, pageNumber, commentQuantity));
    }

    @GetMapping("/by-user")
    public ResponseEntity<CommentResponse> getAllCommentsByUser(@RequestParam String userName,
                                                                @RequestParam int pageNumber,
                                                                @RequestParam int commentQuantity) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsByUser(userName, pageNumber, commentQuantity));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentPayload> getComment(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(commentId));
    }

    @GetMapping("parent-comment-id/{parentCommentId}")
    public ResponseEntity<List<CommentPayload>> getSubCommentsByPostAndParentComment(@PathVariable Long parentCommentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getCommentsByPostAndParentComment(parentCommentId));
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
