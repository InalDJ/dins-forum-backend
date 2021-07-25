package com.java.springportfolio.entity;

import com.java.springportfolio.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum VoteCategory {
    POSTVOTE("post"), COMMENTVOTE("comment");

    private final String category;

    public static VoteCategory lookup(String category) {
        return Arrays.stream(VoteCategory.values())
                .filter(value -> value.getCategory().equals(category))
                .findAny()
                .orElseThrow(() -> new ItemNotFoundException("Vote category not found"));
    }
}
