package com.java.springportfolio.service;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.exception.PortfolioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VoteServiceProvider {

    private final PostVoteService postVoteService;
    private final CommentVoteService commentVoteService;

    @Autowired
    public VoteServiceProvider(PostVoteService postVoteService, CommentVoteService commentVoteService) {
        this.postVoteService = postVoteService;
        this.commentVoteService = commentVoteService;
    }

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
