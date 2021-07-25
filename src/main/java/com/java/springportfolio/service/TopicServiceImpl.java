package com.java.springportfolio.service;

import com.java.springportfolio.dao.TopicRepository;
import com.java.springportfolio.dao.UserRepository;
import com.java.springportfolio.dto.TopicRequest;
import com.java.springportfolio.dto.TopicResponse;
import com.java.springportfolio.entity.Topic;
import com.java.springportfolio.entity.User;
import com.java.springportfolio.exception.ItemAlreadyExistsException;
import com.java.springportfolio.exception.ItemNotFoundException;
import com.java.springportfolio.exception.PortfolioException;
import com.java.springportfolio.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicMapper topicMapper;
    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public void updateTopic(TopicRequest topicRequest) {
        if (topicRequest.getTopicId() == null) {
            log.error("Topic id is null!");
            throw new PortfolioException("Topic id is null!");
        }
        log.info("Updating topic with id: '{}'...", topicRequest.getTopicId());
        Topic existingTopicById = topicRepository.findById(topicRequest.getTopicId())
                .orElseThrow(() -> new ItemNotFoundException("Topic with id: " + topicRequest.getTopicId() + " has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (existingTopicById.getUser().getUserId() != currentUser.getUserId()) {
            log.error("Only topic creator can update topics!");
            throw new PortfolioException("Only topic creators can update topics!");
        }
        Topic existingTopicByName = topicRepository.findByName(topicRequest.getName());
        if (existingTopicByName != null && existingTopicByName.getId() != existingTopicById.getId()) {
            log.error("Topic with name: '{}' already exists!", existingTopicByName.getName());
            throw new ItemAlreadyExistsException("Topic with name: " + topicRequest.getName() + " already exists!");
        }
        topicRepository.save(topicMapper.mapToUpdateExistingTopic(topicRequest, existingTopicById));
        log.info("Topic with id: '{}' has been updated!", existingTopicById.getId());
    }

    @Override
    public void createTopic(TopicRequest topicRequest) {
        log.info("Creating a topic...");
        if (topicRepository.existsByName(topicRequest.getName())) {
            log.error("Topic with name: '{}' already exists!", topicRequest.getName());
            throw new ItemAlreadyExistsException("Topic with the name: " + topicRequest.getName() + " already exists!");
        }
        User currentUser = authService.getCurrentUser();
        topicRepository.save(topicMapper.mapToCreateNewTopic(topicRequest, currentUser));
        log.info("The topic with name: '{}' has been created!", topicRequest.getName());
    }

    @Override
    public List<TopicResponse> getAllTopicsSortedByCreationDate() {
        List<Topic> topics = topicRepository.findAllTopicsOrderByCreatedDate();
        if (topics.isEmpty()) {
            throw new ItemNotFoundException("There are no existing topics!");
        }
        return topics.stream().map(topicMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<TopicResponse> getTopicsSortedByNumberOfPosts() {
        List<Topic> topics = topicRepository.findAllTopicsOrderByNumberOfPosts();
        if (topics.isEmpty()) {
            throw new ItemNotFoundException("There are no existing topics!");
        }
        return topics.stream().map(topicMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public TopicResponse getTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Topic not found!"));
        return topicMapper.mapToDto(topic);
    }

    @Override
    public void deleteTopic(Long id) {
        if (!topicRepository.existsById(id)) {
            log.error("Topic with id: '{}' does not exist in the database!", id);
            throw new ItemNotFoundException("Topic doesn't exist!");
        }
        topicRepository.deleteById(id);
        log.info("The topic with id: '{}' has been removed from the database!", id);
    }
}
