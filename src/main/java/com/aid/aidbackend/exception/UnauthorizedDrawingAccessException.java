package com.aid.aidbackend.exception;

public class UnauthorizedDrawingAccessException extends RuntimeException{

    public UnauthorizedDrawingAccessException(String message) {
        super(message);
    }
}
