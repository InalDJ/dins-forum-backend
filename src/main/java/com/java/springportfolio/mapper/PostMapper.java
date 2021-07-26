package com.java.springportfolio.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.java.springportfolio.dto.PostRequest;
import com.java.springportfolio.dto.PostResponse;
import com.java.springportfolio.entity.Post;
import com.java.springportfolio.entity.Topic;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.service.PostVoteService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import static com.java.springportfolio.entity.VoteType.DOWNVOTE;
import static com.java.springportfolio.entity.VoteType.UPVOTE;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    private PostVoteService postVoteService;

    protected PostMapper() {
    }

    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "description", source = "postRequest.description")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "voteCount", constant = "0")
    @Mapping(target = "topic", source = "topic")
    public abstract Post mapToCreateNewPost(PostRequest postRequest, User user, Topic topic);

    @Mapping(target = "postName", source = "postRequest.postName")
    @Mapping(target = "description", source = "postRequest.description")
    public abstract Post mapToUpdateExistingPost(PostRequest postRequest, @MappingTarget Post post);

    @Mapping(target = "id", source = "post.postId")
    @Mapping(target = "topicName", source = "post.topic.name")
    @Mapping(target = "userName", source = "post.user.username")
    @Mapping(target = "voteCount", source = "post.voteCount")
    @Mapping(target = "description", source = "post.description")
    @Mapping(target = "duration", expression = "java(getDuration(post))")
    @Mapping(target = "createdDate", expression = "java(post.getCreatedDate().toString())")
    @Mapping(target = "upVote", expression = "java(isPostUpVoted(post))")
    @Mapping(target = "downVote", expression = "java(isPostDownVoted(post))")
    public abstract PostResponse mapToDto(Post post);

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }

    boolean isPostUpVoted(Post post) {
        return postVoteService.checkVoteType(post, UPVOTE);
    }

    boolean isPostDownVoted(Post post) {
        return postVoteService.checkVoteType(post, DOWNVOTE);
    }


}

