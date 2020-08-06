package com.example.blog.exception;

public class TopicNotFoundException extends RuntimeException {
    public TopicNotFoundException(String exMessage){
        super(exMessage);
    }
}
