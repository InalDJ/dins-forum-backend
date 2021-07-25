package com.java.springportfolio.service;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.exception.PortfolioException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteServiceProvider {

    private final PostVoteService postVoteService;
    private final CommentVoteService commentVoteService;

    public VoteCategoryService provide(VoteRequest voteRequest) {
        switch (voteRequest.getVoteCategory()) {
            case POSTVOTE:
                return postVoteService;
            case COMMENTVOTE:
                return commentVoteService;
            default:
                throw new PortfolioException("Vote category has not been defined!");
        }
    }
}
