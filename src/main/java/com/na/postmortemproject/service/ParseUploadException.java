package com.na.postmortemproject.service;

public class ParseUploadException extends RuntimeException {

    public ParseUploadException(String message) {
        super(message);
    }

    public ParseUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}