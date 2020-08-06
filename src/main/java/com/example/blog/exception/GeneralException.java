package com.example.blog.exception;

public class GeneralException extends RuntimeException {
    public GeneralException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public GeneralException(String exMessage) {
        super(exMessage);
    }
}
