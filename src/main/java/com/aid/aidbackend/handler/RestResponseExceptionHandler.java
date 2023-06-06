package com.aid.aidbackend.handler;

import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.ApiUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class RestResponseExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateMemberException.class)
    protected ResponseEntity<ApiResult<?>> handleDuplicateMemberException(DuplicateMemberException e) {
        return ResponseEntity.status(CONFLICT).body(ApiUtils.failed(e));
    }

    /**
     * 데이터베이스에서 로그인 정보를 찾을 수 없는 경우
     */
    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ApiResult<Exception> handleAuthenticationException(UsernameNotFoundException e) {
        return ApiUtils.failed(e);
    }
}
