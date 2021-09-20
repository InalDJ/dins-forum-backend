package com.java.springportfolio.entity;

import com.java.springportfolio.exception.ItemNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum VoteCategory {
    POSTVOTE,
    COMMENTVOTE;

    public static VoteCategory lookup(String category) {
        return Arrays.stream(VoteCategory.values())
                .filter(value -> value.name().equals(category))
                .findAny()
                .orElseThrow(() -> new ItemNotFoundException("Vote category not found"));
    }
}
