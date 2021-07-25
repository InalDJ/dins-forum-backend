package com.java.springportfolio.controller;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    public ResponseEntity<Object> vote(@RequestBody VoteRequest voteRequest) {
        voteService.vote(voteRequest);
        return new ResponseEntity<>("You have successfully voted!", HttpStatus.OK);
    }
}
