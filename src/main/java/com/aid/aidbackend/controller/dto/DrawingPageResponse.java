package com.aid.aidbackend.controller.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record DrawingPageResponse(
        Long id,
        String drawingUrl,
        LocalDateTime createdAt
) {
}
