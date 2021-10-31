package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostPayload {

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
    private Integer commentCount;
    @Nullable
    private List<FilePayload> files;
}
