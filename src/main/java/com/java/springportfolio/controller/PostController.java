package com.java.springportfolio.controller;

import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.service.PostService;
import com.java.springportfolio.util.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.java.springportfolio.util.DtoValidator.validatePostRequest;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        validatePostRequest(postRequest);
        postService.createPost(postRequest);
        return new ResponseEntity<>("A new post has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updatePost(@RequestBody PostRequest postRequest) {
        validatePostRequest(postRequest);
        postService.updatePost(postRequest);
        return new ResponseEntity<>("The post has been successfully updated!", HttpStatus.OK);
    }

    @GetMapping("/newest")
    public ResponseEntity<List<PostResponse>> getAllPostsSortedByCreationDate() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostsSortedByCreationDate());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostResponse>> getAllPostsSortedByNumberOfVotes() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostsSortedByVoteCount());
    }

    @GetMapping("/postsByTopic/{topic}")
    public ResponseEntity<List<PostResponse>> getAllPostsSortedByTopic(@PathVariable String topic) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostsByTopic(topic));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>("The post has been successfully deleted!", HttpStatus.OK);
    }
}
