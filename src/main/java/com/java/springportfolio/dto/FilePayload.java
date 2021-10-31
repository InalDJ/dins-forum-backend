package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FilePayload {

    @Nullable
    private Long id;
    private String url;
    private String path;
    @Nullable
    private Long postId;
    @Nullable
    private Long commentId;
}
