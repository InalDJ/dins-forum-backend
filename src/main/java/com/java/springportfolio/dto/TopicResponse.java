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
public class TopicResponse {

    private List<TopicPayload> topics;
    private int numberOfElementsPerPage;
    private int totalPages;
    private int pageNumber;
    private long numberOfElementsTotal;
}
