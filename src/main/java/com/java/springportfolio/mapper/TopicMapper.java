package com.java.springportfolio.mapper;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.java.springportfolio.dto.TopicPayload;
import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;
import com.java.springportfolio.entity.Topic;
import com.java.springportfolio.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TopicMapper {

    @Mapping(target = "name", source = "topicRequest.name")
    @Mapping(target = "description", source = "topicRequest.description")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    public abstract Topic mapToCreateNewTopic(TopicRequest topicRequest, User user);

    @Mapping(target = "topic.name", source = "topicRequest.name")
    @Mapping(target = "topic.description", source = "topicRequest.description")
    public abstract Topic mapToUpdateExistingTopic(TopicRequest topicRequest, @MappingTarget Topic topic);

    @Mapping(target = "id", source = "topic.id")
    @Mapping(target = "name", source = "topic.name")
    @Mapping(target = "description", source = "topic.description")
    @Mapping(target = "duration", expression = "java(getDuration(topic))")
    @Mapping(target = "createdDate", expression = "java(topic.getCreatedDate().toString())")
    @Mapping(target = "userName", source = "topic.user.username")
    @Mapping(target = "numberOfPosts", expression = "java(topic.getPosts().size())")
    public abstract TopicPayload mapToTopicPayload(Topic topic);

    public TopicResponse mapToTopicResponse(Page<Topic> topicPage) {
        List<TopicPayload> topicPayload = topicPage.getContent().stream().map(this::mapToTopicPayload).collect(Collectors.toList());
        return TopicResponse.builder()
                .topics(topicPayload)
                .totalPages(topicPage.getTotalPages())
                .numberOfElementsPerPage(topicPage.getNumberOfElements())
                .numberOfElementsTotal(topicPage.getTotalElements())
                .pageNumber(topicPage.getNumber())
                .build();
    }

    String getDuration(Topic topic) {
        return TimeAgo.using(topic.getCreatedDate().toEpochMilli());
    }
}
