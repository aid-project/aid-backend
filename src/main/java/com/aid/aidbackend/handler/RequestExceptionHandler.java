package com.aid.aidbackend.handler;

import com.aid.aidbackend.utils.ApiResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.aid.aidbackend.utils.ApiUtils.failed;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class RequestExceptionHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiResult<Exception> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return failed(e);
    }

}
