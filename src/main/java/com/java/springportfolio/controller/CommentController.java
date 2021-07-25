package com.java.springportfolio.controller;

import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.java.springportfolio.util.DtoValidator.validateCommentRequest;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
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
        validateCommentRequest(commentRequest);
        commentService.createComment(commentRequest);
        return new ResponseEntity<>("A new comment has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateComment(@RequestBody CommentRequest commentRequest) {
        validateCommentRequest(commentRequest);
        commentService.updateComment(commentRequest);
        return new ResponseEntity<>("A new comment has been created!", HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("The comment has been successfully deleted!", HttpStatus.OK);
    }
}
