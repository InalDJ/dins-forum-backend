package com.java.springportfolio.service;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.util.DtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

    private final VoteServiceProvider voteServiceProvider;
    private final DtoValidator dtoValidator;

    @Autowired
    public VoteServiceImpl(VoteServiceProvider voteServiceProvider, DtoValidator dtoValidator) {
        this.voteServiceProvider = voteServiceProvider;
        this.dtoValidator = dtoValidator;
    }

    @Override
    public void vote(VoteRequest voteRequest) {
        dtoValidator.validateVoteRequest(voteRequest);
        VoteCategoryService voteCategoryService = voteServiceProvider.provide(voteRequest);
        voteCategoryService.vote(voteRequest);
    }
}
