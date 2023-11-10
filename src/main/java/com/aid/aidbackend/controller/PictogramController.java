package com.aid.aidbackend.controller;

import com.aid.aidbackend.controller.dto.PictogramResponse;
import com.aid.aidbackend.service.PictogramService;
import com.aid.aidbackend.utils.ApiResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.aid.aidbackend.utils.ApiUtils.succeed;

@RestController
@RequestMapping("/api/v1/pictograms")
@RequiredArgsConstructor
public class PictogramController {

    private final PictogramService pictogramService;

    @GetMapping("/{drawingId}")
    public ApiResult<List<PictogramResponse>>readPictogramInfo(@PathVariable long drawingId) {

        return succeed(
                pictogramService.findPictograms(drawingId)
        );
    }
}
