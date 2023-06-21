package com.aid.aidbackend.utils;

import lombok.Getter;

@Getter
public class ApiResult<T> {

    private T data;
    private String error;

    ApiResult() {
        /* no-op */
    }

    ApiResult(T data, String error) {
        this.data = data;
        this.error = error;
    }
}