package com.java.springportfolio.dto;

import com.java.springportfolio.entity.VoteType;

public class CommentVoteRequest {

    private VoteType voteType;
    private Long commentId;

    public VoteType getVoteType() {
        return voteType;
    }

    public void setVoteType(VoteType voteType) {
        this.voteType = voteType;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
