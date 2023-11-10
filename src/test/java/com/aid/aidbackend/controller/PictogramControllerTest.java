package com.aid.aidbackend.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("")
class PictogramControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("[픽토그램 조회] 그림ID를 가진 픽토그램을 요청한 픽토그램을 출력한다.")
    void test_01() throws Exception {
        // given
        String drawingId = "1";
        MockHttpServletRequestBuilder request = get("/api/v1/pictograms/" + drawingId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(request);

        // then
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").isNumber())
                .andExpect(jsonPath("$.data[1].drawing_id").isNumber())
                .andExpect(jsonPath("$.data[2].pictogram_url").isString())
                .andExpect(jsonPath("$.error").doesNotExist());

    }

    @Test
    @DisplayName("[픽토그램 조회] 해당 그림ID를 가진 픽토그램이 없다면 WrongPictogramDataException을 발생한다.")
    void test_02() throws Exception {
        // given

        String drawingId = "-1";
        MockHttpServletRequestBuilder request = get("/api/v1/pictograms/" + drawingId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        // when
        ResultActions result = mockMvc.perform(request);

        // then
        result.andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").isEmpty())
                .andExpect(jsonPath("$.error").exists());
    }
}
