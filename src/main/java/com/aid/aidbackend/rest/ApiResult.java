package com.aid.aidbackend.rest;

import com.google.common.base.MoreObjects;

public class ApiResult<T> {

    private final T data;

    private final String error;

    ApiResult(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public static <T> ApiResult<T> succeed(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<T> failed(T data, String error) {
        return new ApiResult<>(data, error);
    }

    public static <T> ApiResult<T> failed(T data, Throwable throwable) {
        return new ApiResult<>(data, throwable.getMessage());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("data", data)
                .add("error", error)
                .toString();
    }
}
