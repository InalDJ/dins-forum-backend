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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
            throw new PortfolioException("Topic id is null!");
        }
        Topic existingTopicById = topicRepository.findById(topicRequest.getTopicId())
                .orElseThrow(() -> new ItemNotFoundException("Topic with id: " + topicRequest.getTopicId() + " has not been found!"));
        User currentUser = authService.getCurrentUser();
        if (existingTopicById.getUser().getUserId() != currentUser.getUserId()) {
            throw new PortfolioException("Only topic creators can update topics!");
        }
        Topic existingTopicByName = topicRepository.findByName(topicRequest.getName());
        if (existingTopicByName != null && existingTopicByName.getId() != existingTopicById.getId()) {
            throw new ItemAlreadyExistsException("Topic with the name: " + topicRequest.getName() + " already exists!");
        }
        topicRepository.save(topicMapper.mapToUpdateExistingTopic(topicRequest, existingTopicById));
    }

    //don't forget to a use user from authentication
    @Override
    public void createTopic(TopicRequest topicRequest) {
        if (topicRepository.existsByName(topicRequest.getName())) {
            throw new ItemAlreadyExistsException("Topic with the name: " + topicRequest.getName() + " already exists!");
        }
        User user = userRepository.findById(topicRequest.getUserId())
                .orElseThrow(() -> new ItemNotFoundException("User with id: " + topicRequest.getUserId() + " has not been found"));
        topicRepository.save(topicMapper.mapToCreateNewTopic(topicRequest, user));
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
            throw new ItemNotFoundException("Topic doesn't exist!");
        }
        topicRepository.deleteById(id);
    }
}
