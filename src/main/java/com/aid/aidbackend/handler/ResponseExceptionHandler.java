package com.aid.aidbackend.handler;

import com.aid.aidbackend.exception.DuplicateMemberException;
import com.aid.aidbackend.exception.UnauthorizedDrawingAccessException;
import com.aid.aidbackend.exception.WrongAuthDataException;
import com.aid.aidbackend.exception.WrongDrawingException;
import com.aid.aidbackend.exception.WrongMemberException;
import com.aid.aidbackend.exception.WrongPictogramDataException;
import com.aid.aidbackend.utils.ApiResult;
import com.aid.aidbackend.utils.ApiUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ResponseExceptionHandler {

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(DuplicateMemberException.class)
    protected ApiResult<Exception> handleDuplicateMemberException(DuplicateMemberException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(WrongAuthDataException.class)
    protected ApiResult<Exception> handleWrongAuthDataException(WrongAuthDataException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }
    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WrongDrawingException.class)
    protected ApiResult<Exception> handleWrongDrawingPageException(WrongDrawingException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(WrongPictogramDataException.class)
    protected ApiResult<Exception> handleWrongPictogramDataException(WrongPictogramDataException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(UnauthorizedDrawingAccessException.class)
    protected ApiResult<Exception> handleUnauthorizedDrawingAccessException(UnauthorizedDrawingAccessException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(WrongMemberException.class)
    protected ApiResult<Exception> handleWrongMemberException(WrongMemberException e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ApiResult<Exception> handleException(Exception e) {
        e.printStackTrace();
        return ApiUtils.failed(e);
    }

}
