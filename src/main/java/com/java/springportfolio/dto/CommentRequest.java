package com.java.springportfolio.dto;

import org.springframework.lang.Nullable;

public class CommentRequest {

    @Nullable
    private Long id;
    private String text;
    @Nullable
    private Long parentCommentId;
    private Long postId;

    @Nullable
    public Long getId() {
        return id;
    }

    public void setId(@Nullable Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    @Nullable
    public Long getParentCommentId() {
        return parentCommentId;
    }

    public Long getPostId() {
        return postId;
    }
}
