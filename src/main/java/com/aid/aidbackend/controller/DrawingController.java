package com.aid.aidbackend.controller;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.controller.dto.DrawingDto;
import com.aid.aidbackend.entity.Drawing;
import com.aid.aidbackend.service.DrawingService;
import com.aid.aidbackend.service.FileService;
import com.aid.aidbackend.utils.ApiResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.aid.aidbackend.utils.ApiUtils.succeed;
import static com.aid.aidbackend.utils.JwtProvider.CURRENT_MEMBER;

@RestController
@RequestMapping("/api/v1/drawings")
@RequiredArgsConstructor
public class DrawingController {

    private final FileService fileService;
    private final DrawingService drawingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public ApiResult<DrawingDto> uploadDrawing(HttpServletRequest request, @RequestParam("drawing") MultipartFile file) {
        CurrentMember currentMember = (CurrentMember) request.getAttribute(CURRENT_MEMBER);

        String uri = fileService.upload(file);

        Drawing result = drawingService.createDrawing(currentMember.memberId(), uri);

        String url = fileService.retrieve(result.getDrawingUrl());

        return succeed(new DrawingDto(url));
    }

}
