package com.aid.aidbackend.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;

import static com.aid.aidbackend.utils.JwtProvider.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("")
class DrawingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[그림 업로드 테스트] 클라이언트가 그린 그림을 저장한다.")
    void test_01() throws Exception {
        /* given */
        String jwt = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");

        Path tempFile = Files.createTempFile("test-image", ".png");

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.png",
                MediaType.IMAGE_PNG_VALUE,
                Files.readAllBytes(tempFile)
        );

        /* when */
        ResultActions result = mockMvc.perform(
                multipart("/api/v1/drawings")
                        .file(file)
                        .header(AUTHORIZATION, BEARER + jwt)
        );

        /* then */
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.drawing_url").isString());
    }

}