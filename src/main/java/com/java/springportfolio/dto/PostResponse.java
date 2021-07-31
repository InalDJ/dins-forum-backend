package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String postName;
    private String description;
    private String userName;
    private String topicName;
    private Integer voteCount;
    private String duration;
    private String createdDate;
    private boolean upVote;
    private boolean downVote;
}


