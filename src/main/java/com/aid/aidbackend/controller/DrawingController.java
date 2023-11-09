package com.aid.aidbackend.controller;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.controller.dto.DrawingPageResponse;
import com.aid.aidbackend.entity.Drawing;
import com.aid.aidbackend.repository.DrawingRepository;
import com.aid.aidbackend.service.DrawingService;
import com.aid.aidbackend.service.FileService;
import com.aid.aidbackend.service.PictogramService;
import com.aid.aidbackend.utils.ApiResult;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.aid.aidbackend.utils.ApiUtils.succeed;
import static com.aid.aidbackend.utils.JwtProvider.CURRENT_MEMBER;

@RestController
@RequestMapping("/api/v1/drawings")
@RequiredArgsConstructor
public class DrawingController {

    private final FileService fileService;
    private final DrawingService drawingService;
    private final PictogramService pictogramService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @Transactional
    public ApiResult<PictogramResponse> uploadDrawing(
            HttpServletRequest request,
            @RequestParam(name = "drawing_img") MultipartFile file,
            @RequestParam(name = "tags") List<String> tags
    ) {
        CurrentMember currentMember = (CurrentMember) request.getAttribute(CURRENT_MEMBER);
        /* 이미지 업로드 */
        String drawingUri = fileService.upload(file);
        Drawing drawing = drawingService.createDrawing(currentMember.memberId(), drawingUri);

        /* 태그 처리 */
//        drawingService.linkTags();

        /* 픽토그램 생성 */
        List<String> pictogramUriList = pictogramService.generatePictograms(drawing.getUri(), tags);
        List<String> pictogramUrlList = pictogramUriList.stream()
                .map(fileService::retrieve)
                .toList();

        return succeed(new PictogramResponse(pictogramUrlList));
    }

    @GetMapping("/list")
    public ApiResult<List<DrawingPageResponse>> drawingList(@RequestParam(value = "page") int pageNumber) {
        List<DrawingPageResponse> drawings = drawingService.getDrawings(pageNumber);
        return succeed(drawings);
    }


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record PictogramResponse(
            List<String> pictogramUrl
    ) {
    }

}
