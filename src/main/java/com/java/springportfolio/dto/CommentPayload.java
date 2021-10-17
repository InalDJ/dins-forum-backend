package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentPayload {
    private Long id;
    private String text;
    private Integer voteCount;
    @Nullable
    private Boolean upVoted;
    @Nullable
    private Boolean downVoted;
    @Nullable
    private Long parentCommentId;
    private Instant createdDate;
    private String duration;
    private String userName;
    private Long postId;
    private Integer subCommentCount;
}
