package com.aid.aidbackend.exception;

public class WrongAuthDataException extends RuntimeException {

    public WrongAuthDataException(String message) {
        super(message);
    }
    
}
