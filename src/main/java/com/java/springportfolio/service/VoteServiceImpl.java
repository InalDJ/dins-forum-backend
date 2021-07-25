package com.java.springportfolio.service;


import com.java.springportfolio.dto.VoteRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.java.springportfolio.util.DtoValidator.validateVoteRequest;


@Service
public class VoteServiceImpl implements VoteService {

    private final VoteServiceProvider voteServiceProvider;

    @Autowired
    public VoteServiceImpl(VoteServiceProvider voteServiceProvider) {
        this.voteServiceProvider = voteServiceProvider;
    }

    @Override
    public void vote(VoteRequest voteRequest) {
        validateVoteRequest(voteRequest);
        VoteCategoryService voteCategoryService = voteServiceProvider.provide(voteRequest);
        voteCategoryService.vote(voteRequest);
    }
}
