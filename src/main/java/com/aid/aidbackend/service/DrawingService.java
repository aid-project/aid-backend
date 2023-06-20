package com.aid.aidbackend.service;

import com.aid.aidbackend.entity.Drawing;
import com.aid.aidbackend.repository.DrawingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;

    public Drawing createDrawing(Long memberId, String drawingUrl) {
        Drawing drawing = Drawing.builder()
                .memberId(memberId)
                .drawingUrl(drawingUrl)
                .build();

        return drawingRepository.save(drawing);
    }

}
