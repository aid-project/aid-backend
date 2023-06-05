package com.aid.aidbackend.utils;

import lombok.Getter;

@Getter
public final class ApiResult<T> {

    private final T data;
    private final String error;

    ApiResult(T data, String error) {
        this.data = data;
        this.error = error;
    }
}