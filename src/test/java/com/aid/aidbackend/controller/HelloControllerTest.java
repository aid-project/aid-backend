package com.aid.aidbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Hello controller 통합 테스트")
class HelloControllerTest {

    private MockMvc mockMvc;

    @Autowired
    public void setMockMvc(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("[Hello API] API 서버 응답을 테스트한다.")
    void test_01() throws Exception {
        /* given */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/hello");

        /* when */
        ResultActions result = mockMvc.perform(request);

        /* then */
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[PATH 테스트] HTTP path를 잘못 입력한 경우 API 요청을 실패한다.")
    void test_02() throws Exception {
        /* given */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/api/v1/helloo");

        /* when */
        ResultActions result = mockMvc.perform(request);


        /* then */
        result.andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("[METHOD 테스트] HTTP method를 잘못 입력한 경우 API 요청을 실패한다.")
    void test_03() throws Exception {
        /* given */
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/api/v1/hello");

        /* when */
        ResultActions result = mockMvc.perform(request);

        /* then */
        result.andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }


}