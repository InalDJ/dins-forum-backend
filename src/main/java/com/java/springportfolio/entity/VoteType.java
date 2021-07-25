package com.java.springportfolio.entity;

import com.java.springportfolio.exception.ItemNotFoundException;

import java.util.EnumSet;

public enum VoteType {
    UPVOTE,
    DOWNVOTE;

    public static VoteType lookup(String voteType) {
        for(var value : EnumSet.allOf(VoteType.class)){
            if(value.name().equals(voteType))
                return value;
        }
        throw new ItemNotFoundException("Vote type has not been found!");
    }
}
