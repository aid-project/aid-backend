package com.aid.aidbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Member controller 통합 테스트")
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[회원정보 조회] 회원정보 조회를 요청한 클라이언트의 회원 정보를 출력한다. ")
    void test_01() throws Exception {
        /* given */
        String jwt = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");

        MockHttpServletRequestBuilder request = get("/api/v1/members")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        /* when */
        ResultActions result = mockMvc.perform(request);

        /* then */
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").isString())
                .andExpect(jsonPath("$.data.nickname").isString())
                .andExpect(jsonPath("$.data.profile_url").hasJsonPath())
                .andExpect(jsonPath("$.data.registered_at").exists())
                .andExpect(jsonPath("$.error").doesNotExist());
    }

    @Test
    @DisplayName("[비밀번호 변경] 클라이언트의 비밀번호를 변경한다.")
    void test_02() throws Exception {
        /* given */
        String jwt = "";
        String password = "";
        String newPassword = "";
        Assertions.assertNotEquals("", jwt, "토큰 데이터가 없습니다.");
        Map<String, String> input = new HashMap<>();
        input.put("password", password);
        input.put("new_password", newPassword);

        String content = objectMapper.writeValueAsString(input);
        MockHttpServletRequestBuilder request = patch("/api/v1/members/password")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .content(content);

        /* when */
        ResultActions result = mockMvc.perform(request);

        /* then */
        result.andDo(print())
                .andExpect(status().isCreated());
    }

}