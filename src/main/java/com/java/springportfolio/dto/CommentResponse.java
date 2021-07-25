package com.java.springportfolio.dto;

import org.springframework.lang.Nullable;

import java.time.Instant;

public class CommentResponse {
    private Long id;
    private String text;
    private Integer voteCount;
    @Nullable
    private Boolean upVoted;
    @Nullable
    private Boolean downVoted;
    @Nullable
    private Long parentCommentId;
    private Instant createdDate;
    private String duration;
    private String userName;
    private Long postId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    @Nullable
    public Long getParentCommentId() {
        return parentCommentId;
    }

    public void setParentCommentId(@Nullable Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Nullable
    public Boolean getUpVoted() {
        return upVoted;
    }

    public void setUpVoted(@Nullable Boolean upVoted) {
        this.upVoted = upVoted;
    }

    @Nullable
    public Boolean getDownVoted() {
        return downVoted;
    }

    public void setDownVoted(@Nullable Boolean downVoted) {
        this.downVoted = downVoted;
    }
}
