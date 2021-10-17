package com.java.springportfolio.controller;

import com.java.springportfolio.dto.PostPayload;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.service.PostService;
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

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final DtoValidator dtoValidator;

    @PostMapping
    public ResponseEntity<String> createPost(@RequestBody PostRequest postRequest) {
        dtoValidator.validatePostRequest(postRequest);
        postService.createPost(postRequest);
        return new ResponseEntity<>("A new post has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updatePost(@RequestBody PostRequest postRequest) {
        dtoValidator.validatePostRequest(postRequest);
        postService.updatePost(postRequest);
        return new ResponseEntity<>("The post has been successfully updated!", HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<PostResponse> getAllPosts(@RequestParam String orderType,
                                                    @RequestParam int pageNumber,
                                                    @RequestParam int postsPerPage) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts(orderType, pageNumber, postsPerPage));
    }

    @GetMapping("/topics/{topicName}")
    public ResponseEntity<PostResponse> getAllPostsByTopic(@PathVariable String topicName,
                                                           @RequestParam String orderType,
                                                           @RequestParam int pageNumber,
                                                           @RequestParam int postsPerPage) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPostsByTopic(topicName, orderType, pageNumber, postsPerPage));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostPayload> getPost(@PathVariable Long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        return new ResponseEntity<>("The post has been successfully deleted!", HttpStatus.OK);
    }
}
