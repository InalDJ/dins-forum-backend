package com.java.springportfolio.controller;

import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;
import com.java.springportfolio.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.java.springportfolio.util.DtoValidator.validateTopicRequest;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @PostMapping
    public ResponseEntity<String> createTopic(@RequestBody TopicRequest topicRequest) {
        validateTopicRequest(topicRequest);
        topicService.createTopic(topicRequest);
        return new ResponseEntity<>("A new topic has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateTopic(@RequestBody TopicRequest topicRequest) {
        validateTopicRequest(topicRequest);
        topicService.updateTopic(topicRequest);
        return new ResponseEntity<>("The topic has been successfully updated!", HttpStatus.OK);
    }

    @GetMapping("/newest")
    public ResponseEntity<List<TopicResponse>> getAllTopicsSortedByCreationDate() {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getAllTopicsSortedByCreationDate());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<TopicResponse>> getAllTopicsSortedByNumberOfPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getTopicsSortedByNumberOfPosts());
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicResponse> getTopic(@PathVariable Long topicId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getTopic(topicId));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<String> deleteTopic(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
        return new ResponseEntity<>("The topic has been successfully deleted!", HttpStatus.OK);
    }
}
