package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TopicPayload {

    private Long id;
    private String name;
    private String description;
    private String createdDate;
    private String userName;
    private Integer numberOfPosts;
    private String duration;
}
