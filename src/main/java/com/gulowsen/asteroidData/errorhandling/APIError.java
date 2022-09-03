package com.gulowsen.asteroidData.errorhandling;

import org.springframework.http.HttpStatus;

public class APIError {

    private HttpStatus status;
    private String message;

    public APIError(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
