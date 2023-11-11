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

import static com.aid.aidbackend.utils.JwtProvider.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

        /* given */
        MockMultipartFile file = new MockMultipartFile("drawing_img", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test".getBytes());
        String[] tags = {"tag1", "tag2", "tag3"};

        /* when */
        ResultActions result = mockMvc.perform(
                multipart("/api/v1/drawings")
                        .file(file)
                        .header(AUTHORIZATION, BEARER + jwt)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("tag", tags)
        );

        /* then */
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.drawing_url").isString());
    }

    @Test
    @DisplayName("[그림 조회 테스트] 클라이언트가 private 설정한 것을 제외하고 그림들을 페이지 단위로 읽을 수 있다.")
    void test_02() throws Exception {
        /* given */
        String pageParam = "0";
        /* when */
        ResultActions result = mockMvc.perform(
                get("/api/v1/drawings/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", pageParam)
        );
        /* then */
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").isNumber())
                .andExpect(jsonPath("$.data[1].drawing_url").isString())
                .andExpect(jsonPath("$.data[2].created_at").isString())
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    @DisplayName("[그림 조회 테스트] 만약 param을 잘못된 값을 전달할 경우 예외가 발생된다.")
    void test_03() throws Exception {
        /* given */
        String pageParam = "-1";
        /* when */
        ResultActions result = mockMvc.perform(
                get("/api/v1/drawings/list")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", pageParam)
        );
        /* then */
        result.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    @DisplayName("[그림 조회 테스트] 토큰을 기반으로 회원을 찾아 자신의 그림만 볼 수 있다.")
    void test_04() throws Exception {
        /* given */
        String pageParam = "0";
        String jwt = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");
        /* when */
        ResultActions result = mockMvc.perform(
                get("/api/v1/drawings/my")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, BEARER + jwt)
                        .param("page", pageParam)
        );
        /* then */
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").isNumber())
                .andExpect(jsonPath("$.data[1].is_private").isBoolean())
                .andExpect(jsonPath("$.data[2].drawing_url").isString())
                .andExpect(jsonPath("$.data[3].created_at").isString())
                .andExpect(jsonPath("$.error").isEmpty());
    }

    @Test
    @DisplayName("[그림 공개여부 변경 테스트] 그림 ID에 따른 공개여부를 변경할 수 있다. 또한, 자신의 것만 가능하다.")
    void test_05() throws Exception {
        /* given */
        String drawingId = "";
        String jwt = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");
        /* when */
        ResultActions result = mockMvc.perform(
                patch("/api/v1/drawings/my/" + drawingId)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header(AUTHORIZATION, BEARER + jwt)
                        .param("is_private", "false")
        );
        /* then */
        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isString())
                .andExpect(jsonPath("$.error").isEmpty());
    }


    @Test
    @Transactional
    @DisplayName("[그림 삭제 테스트] 그림 ID에 맞는 그림을 삭제 가능하다. 자신의 것만 가능하며, 관계된 픽토그램들도 삭제된다.")
    void test_06() throws Exception {
        /* given */
        String drawingId = "14";
        String jwt = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");
        /* when */
        ResultActions result = mockMvc.perform(
                delete("/api/v1/drawings/my/" + drawingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(AUTHORIZATION, BEARER + jwt)
        );
        /* then */
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isString())
                .andExpect(jsonPath("$.error").isEmpty());
    }
}