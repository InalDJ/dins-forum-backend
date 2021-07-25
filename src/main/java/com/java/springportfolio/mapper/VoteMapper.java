package com.java.springportfolio.mapper;

import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.CommentVote;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.entity.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class VoteMapper {

    @Mapping(target = "voteType", source = "voteRequest.voteType")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    public abstract Vote mapToPostVote(VoteRequest voteRequest, User user, Post post);

    @Mapping(target = "voteType", source = "voteRequest.voteType")
    @Mapping(target = "comment", source = "comment")
    @Mapping(target = "user", source = "user")
    public abstract CommentVote mapToCommentVote(VoteRequest voteRequest, User user, Comment comment);
}
