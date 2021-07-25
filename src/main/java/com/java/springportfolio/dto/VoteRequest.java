package com.java.springportfolio.dto;

import com.java.springportfolio.entity.VoteCategory;
import com.java.springportfolio.entity.VoteType;

public class VoteRequest {

    private VoteCategory voteCategory;
    private VoteType voteType;
    private Long itemId;

    public VoteRequest() {
    }

    public VoteType getVoteType() {
        return voteType;
    }

    public Long getItemId() {
        return itemId;
    }

    public VoteCategory getVoteCategory() {
        return voteCategory;
    }
}
