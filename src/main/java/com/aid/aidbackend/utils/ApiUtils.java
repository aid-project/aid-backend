package com.aid.aidbackend.utils;

public final class ApiUtils {

    private ApiUtils() {
        /* do nothing */
    }

    public static <T> ApiResult<T> succeed(T data) {
        return new ApiResult<>(data, null);
    }

    public static <T> ApiResult<T> failed(String error) {
        return new ApiResult<>(null, error);
    }

    public static <T> ApiResult<T> failed(Throwable throwable) {
        return new ApiResult<>(null, throwable.getMessage());
    }

}
