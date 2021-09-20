package com.java.springportfolio.dto;

import com.java.springportfolio.entity.VoteCategory;
import com.java.springportfolio.entity.VoteType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteRequest {

    private VoteCategory voteCategory;
    private VoteType voteType;
    @Nullable
    private Long postId;
    @Nullable
    private Long commentId;
}
