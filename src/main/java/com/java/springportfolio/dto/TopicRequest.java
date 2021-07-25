package com.java.springportfolio.dto;

import org.springframework.lang.Nullable;

public class TopicRequest {

    @Nullable
    private Long topicId;
    private String name;
    private String description;
    private Long userId;

    @Nullable
    public Long getTopicId() {
        return topicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Long getUserId() {
        return userId;
    }
}
