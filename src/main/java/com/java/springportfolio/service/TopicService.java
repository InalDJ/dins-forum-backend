package com.java.springportfolio.service;

import com.java.springportfolio.dto.TopicPayload;
import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;

import java.util.List;

public interface TopicService {

    void updateTopic(TopicRequest topicRequest);

    void createTopic(TopicRequest topicRequest);

    TopicResponse getAllTopics(String orderType, int pageNumber, int topicsPerPage);

    TopicPayload getTopic(Long id);

    void deleteTopic(Long id);
}
