package com.aid.aidbackend.controller.dto;

import com.aid.aidbackend.entity.Pictogram;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PictogramResponse(
        Long id,
        Long drawingId,
        String pictogramUrl
) {
    public static PictogramResponse of(Pictogram pictogram) {
        return new PictogramResponse(
                pictogram.getId(),
                pictogram.getDrawingId(),
                pictogram.getUri()
        );
    }
}
