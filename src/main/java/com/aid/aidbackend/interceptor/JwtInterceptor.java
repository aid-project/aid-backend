package com.aid.aidbackend.interceptor;

import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;

import static com.aid.aidbackend.json.JsonUtils.toJson;
import static com.aid.aidbackend.utils.ApiUtils.failed;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final String BEARER = "Bearer ";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String bearer = request.getHeader(HttpHeaders.AUTHORIZATION);

        /* 토큰 헤더가 없는 경우 */
        if (Objects.isNull(bearer)) {
            buildResponse(response, SC_UNAUTHORIZED, failed("토큰이 없습니다."));
            return false;
        }

        if (!StringUtils.hasText(BEARER) || !bearer.startsWith(BEARER)) {
            buildResponse(response, SC_UNAUTHORIZED, failed("Bearer 값이 없습니다."));
            return false;
        }

        String token = bearer.substring(7);
        try {
            String memberId = JwtUtils.parseClaims(token).getSubject();
            request.setAttribute("member-id", memberId);
            return true;
        } catch (Exception e) {
            buildResponse(response, SC_UNAUTHORIZED, failed(e));
            return false;
        }
    }

    private void buildResponse(HttpServletResponse response, int status, ApiResult<?> apiResult) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                toJson(
                        apiResult
                )
        );
    }
}
