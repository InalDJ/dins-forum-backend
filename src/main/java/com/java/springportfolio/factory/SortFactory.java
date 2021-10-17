package com.java.springportfolio.factory;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SortFactory {
    private static final String CREATED_DATE_COLUMN = "createdDate";
    private static final String VOTE_COUNT_COLUMN = "voteCount";
    private static final String POSTS_SIZE = "posts.size";

    public static Sort getSortByDateAndVoteCount() {
        return Sort.by(CREATED_DATE_COLUMN).descending().and(Sort.by(VOTE_COUNT_COLUMN)).descending();
    }

    public static Sort getSortByNumberOfPosts() {
        return Sort.by(POSTS_SIZE).descending();
    }

    public static Sort getSortByDate() {
        return Sort.by(CREATED_DATE_COLUMN).descending();
    }

    public static Sort getSortByVoteCount() {
        return Sort.by(VOTE_COUNT_COLUMN).descending();
    }
}
