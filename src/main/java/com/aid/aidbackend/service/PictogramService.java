package com.aid.aidbackend.service;

import com.aid.aidbackend.controller.dto.PictogramResponse;
import com.aid.aidbackend.entity.Pictogram;
import com.aid.aidbackend.exception.WrongPictogramDataException;
import com.aid.aidbackend.repository.DrawingRepository;
import com.aid.aidbackend.repository.PictogramRepository;
import com.aid.aidbackend.utils.ApiResult;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PictogramService {

    @Value("${ai.server.pictogram-path}")
    private String aiServerPictogramPath;

    private final FileService fileService;

    private final WebClient webClient;
    private final PictogramRepository pictogramRepository;
    private final DrawingRepository drawingRepository;

    private final ParameterizedTypeReference<ApiResult<PictogramDto>> typeReference = new ParameterizedTypeReference<>() {
    };

    public List<String> generatePictograms(String drawingUri, List<String> tags) {
        Long drawingId = drawingRepository.findByUri(drawingUri)
                .orElseThrow(() -> new RuntimeException("그림을 찾을 수 없습니다."))
                .getId();

        List<String> pictogramUriList = postAiServer(drawingUri, tags);

        return pictogramUriList.stream()
                .map(uri -> pictogramRepository.save(new Pictogram(drawingId, uri))
                        .getUri()
                )
                .toList();
    }

    public List<PictogramResponse> findPictograms(Long drawingId) {
        List<Pictogram> pictograms = pictogramRepository.findAllByDrawingId(drawingId);
        if (pictograms == null || pictograms.isEmpty()) {
            throw new WrongPictogramDataException("픽토그램 조회에 실패했습니다.");
        }
        return pictograms
                .stream()
                .map(pictogram -> new PictogramResponse(
                        pictogram.getId(),
                        drawingId,
                        fileService.retrieve(pictogram.getUri())
                ))
                .toList();
    }
    private List<String> postAiServer(String drawingUri, List<String> tags) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("drawing_uri", drawingUri);
        for (String tag : tags) {
            formData.add("tag", tag);
        }

        ResponseEntity<ApiResult<PictogramDto>> response = webClient
                .method(HttpMethod.POST)
                .uri(aiServerPictogramPath)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve()
                .toEntity(typeReference)
                .block();

        if (response == null || !response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("픽토그램 생성 실패");
        }

        return response.getBody().getData().pictogramUri();
    }

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    record PictogramDto(
            List<String> pictogramUri
    ) {
    }
}
