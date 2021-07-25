package com.java.springportfolio.mapper;

import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.VoteRequest;
import com.java.springportfolio.entity.*;
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
