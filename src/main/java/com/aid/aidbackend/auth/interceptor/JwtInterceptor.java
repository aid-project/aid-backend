package com.aid.aidbackend.auth.interceptor;

import com.aid.aidbackend.auth.CurrentMember;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;

import static com.aid.aidbackend.json.JsonUtils.toJson;
import static com.aid.aidbackend.utils.ApiUtils.failed;
import static com.aid.aidbackend.utils.JwtProvider.BEARER;
import static com.aid.aidbackend.utils.JwtProvider.CURRENT_MEMBER;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String bearer = request.getHeader(AUTHORIZATION);

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
            Long memberId = Long.valueOf(jwtProvider.parseClaims(token).getSubject());
            request.setAttribute(CURRENT_MEMBER, new CurrentMember(memberId));
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
