package com.java.springportfolio.controller;

import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;
import com.java.springportfolio.service.TopicService;
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
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;
    private final DtoValidator dtoValidator;

    @Autowired
    public TopicController(TopicService topicService, DtoValidator dtoValidator) {
        this.topicService = topicService;
        this.dtoValidator = dtoValidator;
    }

    @PostMapping
    public ResponseEntity<String> createTopic(@RequestBody TopicRequest topicRequest) {
        dtoValidator.validateTopicRequest(topicRequest);
        topicService.createTopic(topicRequest);
        return new ResponseEntity<>("A new topic has been created!", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateTopic(@RequestBody TopicRequest topicRequest) {
        dtoValidator.validateTopicRequest(topicRequest);
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
