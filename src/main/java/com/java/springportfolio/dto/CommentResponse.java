package com.java.springportfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponse {

    List<CommentPayload> comments;
    private int numberOfElementsPerPage;
    private long numberOfElementsTotal;
    private int totalPages;
    private int pageNumber;
}
