package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.DrawingDto;
import com.aid.aidbackend.controller.dto.DrawingPageResponse;
import com.aid.aidbackend.entity.Drawing;
import com.aid.aidbackend.exception.UnauthorizedDrawingAccessException;
import com.aid.aidbackend.exception.WrongDrawingException;
import com.aid.aidbackend.repository.DrawingRepository;
import com.aid.aidbackend.repository.PictogramRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DrawingService {

    private final DrawingRepository drawingRepository;
    private final PictogramRepository pictogramRepository;
    private final FileService fileService;
    private static final int PAGE_SIZE = 9; // 한 페이지당 몇 개의 그림 id를 줄지 정하는 상수

    public Drawing createDrawing(Long memberId, String drawingUrl) {
        Drawing drawing = Drawing.builder()
                .memberId(memberId)
                .uri(drawingUrl)
                .build();

        return drawingRepository.save(drawing);
    }

    public List<DrawingPageResponse> getDrawings(int pageNumber) {
        if (pageNumber < 0) {
            throw new WrongDrawingException("그림 정보를 조회하는데 실패했습니다.");
        }
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return drawingRepository.findAllByIsPrivate(false, pageable)
                .map(drawing -> new DrawingPageResponse(
                        drawing.getId(),
                        fileService.retrieve(drawing.getUri()),
                        drawing.getCreatedAt()
                ))
                .toList();
    }

    public List<DrawingDto> getDrawingsByMemberId(int pageNumber, Long memberId) {
        if (pageNumber < 0) {
            throw new WrongDrawingException("나의 기록 조회 실패.");
        }
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
        return drawingRepository.findAllByMemberId(memberId, pageable)
                .map(drawing -> new DrawingDto(
                        drawing.getId(),
                        drawing.isPrivate(),
                        fileService.retrieve(drawing.getUri()),
                        drawing.getCreatedAt()
                ))
                .toList();
    }

    public void modifyDrawingByDrawingId(boolean isPrivate, Long drawingId, Long memberId) {
        Drawing drawing = drawingRepository.findById(drawingId)
                        .orElseThrow(() -> new WrongDrawingException("공개 여부를 수정하는데 실패하였습니다."));
        if (drawing.getMemberId().equals(memberId)) {
            throw new UnauthorizedDrawingAccessException("작성자만 공개 여부를 수정할 수 있습니다.");
        }
        drawing.updateIsPrivate(isPrivate);

    }

    public void removeDrawingByDrawingId(long drawingId, Long memberId) {
        Drawing drawing = drawingRepository.findById(drawingId)
                .orElseThrow(() -> new WrongDrawingException("그림을 삭제하는데 실패했습니다."));
        if (!drawing.getMemberId().equals(memberId)) {
            throw new UnauthorizedDrawingAccessException("작성자만 자신의 그림을 삭제할 수 있습니다.");
        }
        pictogramRepository.deleteAllByDrawingId(drawingId);
        drawingRepository.deleteById(drawingId);

    }
}
