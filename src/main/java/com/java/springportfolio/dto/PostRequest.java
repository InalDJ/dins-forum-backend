package com.java.springportfolio.dto;



import org.springframework.lang.Nullable;


public class PostRequest {

    @Nullable
    private Long postId;
    private String postName;
    private String description;
    private Long topicId;

    @Nullable
    public Long getPostId() {
        return postId;
    }

    public void setPostId(@Nullable Long postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}
