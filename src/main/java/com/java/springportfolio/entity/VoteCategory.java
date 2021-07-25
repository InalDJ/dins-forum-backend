package com.java.springportfolio.entity;


import com.java.springportfolio.exception.ItemNotFoundException;

import java.util.Arrays;

public enum VoteCategory {
    POSTVOTE("post"), COMMENTVOTE("comment");

    private String category;

    VoteCategory(String category) {
    }

    public static VoteCategory lookup(String category) {
        return Arrays.stream(VoteCategory.values())
                .filter(value -> value.getCategory().equals(category))
                .findAny()
                .orElseThrow(() -> new ItemNotFoundException("Vote category not found"));
    }

    private String getCategory() {
        return category;
    }
}
