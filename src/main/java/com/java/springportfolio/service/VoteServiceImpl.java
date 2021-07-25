package com.java.springportfolio.service;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.util.DtoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteServiceProvider voteServiceProvider;
    private final DtoValidator dtoValidator;

    @Override
    public void vote(VoteRequest voteRequest) {
        dtoValidator.validateVoteRequest(voteRequest);
        VoteCategoryService voteCategoryService = voteServiceProvider.provide(voteRequest);
        voteCategoryService.vote(voteRequest);
    }
}
