package com.aid.aidbackend.controller;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Locale;

import static com.aid.aidbackend.json.JsonUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Auth controller 통합 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Faker faker = new Faker(Locale.KOREA);

    @Test
    @DisplayName("[회원가입 테스트] 유효한 회원 데이터일 경우 회원 가입에 성공한다.")
    void test_01() throws Exception {
        /* given */


        /* when */
        ResultActions result = mockMvc.perform(
                signupRequestBuilder(
                        faker.name().username(),
                        faker.internet().emailAddress(),
                        faker.internet().password()
                )
        );

        /* then */
        result.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("[회원가입 테스트] 닉네임이 중복인 경우 회원가입에 실패한다.")
    void test_02() throws Exception {
        /* given */
        String nickname = faker.name().name();

        /* when */
        ResultActions result1 = mockMvc.perform(
                signupRequestBuilder(
                        nickname,
                        faker.internet().emailAddress(),
                        faker.internet().password()
                )
        );
        ResultActions result2 = mockMvc.perform(
                signupRequestBuilder(
                        nickname,
                        faker.internet().emailAddress(),
                        faker.internet().password()
                )
        );

        /* then */
        result1.andDo(print()) // 정상
                .andExpect(status().isCreated());
        result2.andDo(print()) // 중복 닉네임
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("[회원가입 테스트] 이메일이 중복인 경우 회원가입에 실패한다.")
    void test_03() throws Exception {
        /* given */
        String email = faker.internet().emailAddress();

        /* when */
        ResultActions result1 = mockMvc.perform(
                signupRequestBuilder(
                        faker.name().name(),
                        email,
                        faker.internet().password()
                )
        );
        ResultActions result2 = mockMvc.perform(
                signupRequestBuilder(
                        faker.name().name(),
                        email,
                        faker.internet().password()
                )
        );

        /* then */
        result1.andDo(print()) // 정상
                .andExpect(status().isCreated());
        result2.andDo(print()) // 중복 이메일
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("[로그인 테스트] 유효한 로그인 데이터일 경우 로그인에 성공하고 JWT를 발급받는다.")
    void test_04() throws Exception {
        /* given */
        String email = System.getenv("AID_TEST_EMAIL");
        String password = System.getenv("AID_TEST_PASSWORD");
        assertNotNull(email, "환경 변수에 테스트 이메일 데이터가 없습니다.");
        assertNotNull(password, "환경 변수에 테스트 패스워드 데이터가 없습니다.");

        /* when */
        ResultActions result = mockMvc.perform(
                loginRequestBuilder(
                        email,
                        password
                )
        );

        /* then */
        result.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[로그인 테스트] 이메일이 존재하지 않을 경우 로그인에 실패한다.")
    void test_05() throws Exception {
        /* given */
        String email = "🥲";
        String password = System.getenv("AID_TEST_PASSWORD");
        assertNotNull(password, "환경 변수에 테스트 패스워드 데이터가 없습니다.");

        /* when */
        ResultActions result = mockMvc.perform(
                loginRequestBuilder(email, password)
        );


        /* then */
        result.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    @DisplayName("[로그인 테스트] 비밀번호가 유효하지 않은 경우 로그인에 실패한다.")
    void test_06() throws Exception {
        /* given */
        String email = System.getenv("AID_TEST_EMAIL");
        String password = "🥲";
        assertNotNull(email, "환경 변수에 테스트 이메일 데이터가 없습니다.");

        /* when */
        ResultActions result = mockMvc.perform(
                loginRequestBuilder(email, password)
        );


        /* then */
        result.andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andExpect(jsonPath("$.error").exists());
    }

    private MockHttpServletRequestBuilder signupRequestBuilder(String nickname, String email, String password) {
        return post("/api/v1/auth/signup")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        toJson(
                                new HashMap<String, Object>() {{
                                    put("nickname", nickname);
                                    put("email", email);
                                    put("password", password);
                                }}
                        )
                );
    }

    private MockHttpServletRequestBuilder loginRequestBuilder(String email, String password) {
        return post("/api/v1/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        toJson(
                                new HashMap<String, Object>() {{
                                    put("email", email);
                                    put("password", password);
                                }}
                        )
                );
    }
}