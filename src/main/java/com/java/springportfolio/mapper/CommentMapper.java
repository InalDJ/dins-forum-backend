package com.java.springportfolio.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.entity.*;
import com.java.springportfolio.service.CommentVoteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private CommentVoteService commentVoteService;

    protected CommentMapper() {
    }

    @Mapping(target = "text", source = "commentRequest.text")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "parentCommentId", expression = "java(checkIfCommentHasAParent(commentRequest))")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    public abstract Comment mapToCreateNewComment(CommentRequest commentRequest, User user, Post post);

    @Mapping(target = "text", source = "commentRequest.text")
    public abstract Comment mapToUpdateExistingComment(CommentRequest commentRequest, @MappingTarget Comment comment);

    @Mapping(target = "id", source = "comment.id")
    @Mapping(target = "text", source = "comment.text")
    @Mapping(target = "voteCount", source = "comment.voteCount")
    @Mapping(target = "upVoted", expression = "java(isCommentUpVoted(comment))")
    @Mapping(target = "downVoted", expression = "java(isCommentDownVoted(comment))")
    @Mapping(target = "parentCommentId", source = "comment.parentCommentId")
    @Mapping(target = "createdDate", source = "comment.createdDate")
    @Mapping(target = "duration", expression = "java(getDuration(comment))")
    @Mapping(target = "userName", source = "comment.user.username")
    @Mapping(target = "postId", source = "comment.post.postId")
    public abstract CommentResponse mapToDto(Comment comment);

    String getDuration(Comment comment) {
        return TimeAgo.using(comment.getCreatedDate().toEpochMilli());
    }

    Long checkIfCommentHasAParent(CommentRequest commentRequest) {
        if (commentRequest.getParentCommentId() == null) {
            return null;
        }
        return commentRequest.getParentCommentId();
    }

    boolean isCommentUpVoted(Comment comment) {
        return commentVoteService.checkVoteType(comment, VoteType.UPVOTE);
    }

    boolean isCommentDownVoted(Comment comment) {
        return commentVoteService.checkVoteType(comment, VoteType.DOWNVOTE);
    }
}
