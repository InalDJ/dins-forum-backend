package com.java.springportfolio.controller;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.service.CommentService;
import com.java.springportfolio.util.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final DtoValidator dtoValidator;

    @Autowired
    public CommentController(CommentService commentService, DtoValidator dtoValidator) {
        this.commentService = commentService;
        this.dtoValidator = dtoValidator;
    }

    @GetMapping("/newest")
    public ResponseEntity<List<CommentResponse>> getAllCommentsSortedByCreationDate() {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsSortedByCreationDate());
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@PathVariable Long commentId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComment(commentId));
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
        return new ResponseEntity<>("A new comment has been created!", HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("The comment has been successfully deleted!", HttpStatus.OK);
    }
}
