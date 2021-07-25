package com.java.springportfolio.service;

import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;

import java.util.List;

public interface TopicService {

    void updateTopic(TopicRequest topicRequest);

    void createTopic(TopicRequest topicRequest);

    List<TopicResponse> getAllTopicsSortedByCreationDate();

    List<TopicResponse> getTopicsSortedByNumberOfPosts();

    TopicResponse getTopic(Long id);

    void deleteTopic(Long id);
}
