package com.java.springportfolio.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.java.springportfolio.dto.CommentPayload;
import com.java.springportfolio.dto.CommentRequest;
import com.java.springportfolio.dto.CommentResponse;
import com.java.springportfolio.entity.Comment;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.entity.VoteType;
import com.java.springportfolio.service.CommentVoteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {

    @Autowired
    private CommentVoteService commentVoteService;

    protected CommentMapper() {
    }

    @Mapping(target = "text", source = "commentRequest.text")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "parentCommentId", expression = "java(checkIfCommentHasAParent(commentRequest))")
    @Mapping(target = "subCommentCount", constant = "0")
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
    @Mapping(target = "subCommentCount", source = "comment.subCommentCount")
    public abstract CommentPayload mapToCommentPayloadDto(Comment comment);

    public CommentResponse mapToCommentResponse(Page<Comment> commentPage) {
        List<CommentPayload> commentPayload = commentPage.getContent().stream().map(this::mapToCommentPayloadDto).collect(Collectors.toList());
        return CommentResponse.builder()
                .comments(commentPayload)
                .totalPages(commentPage.getTotalPages())
                .numberOfElementsPerPage(commentPage.getNumberOfElements())
                .numberOfElementsTotal(commentPage.getTotalElements())
                .pageNumber(commentPage.getNumber())
                .build();
    }

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
