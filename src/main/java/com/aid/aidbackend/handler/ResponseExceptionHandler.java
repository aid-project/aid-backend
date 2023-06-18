package com.aid.aidbackend.handler;

import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.exception.WrongAuthDataException;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.ApiUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateMemberException.class)
    protected ApiResult<Exception> handleDuplicateMemberException(DuplicateMemberException e) {
        return ApiUtils.failed(e);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(WrongAuthDataException.class)
    protected ApiResult<Exception> handleWrongAuthDataException(WrongAuthDataException e) {
        return ApiUtils.failed(e);
    }

}
