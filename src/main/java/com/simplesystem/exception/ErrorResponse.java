package com.simplesystem.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final int status;
    private final String message;
    private final HttpStatus error;
    private String timestamp;

    public ErrorResponse(HttpStatus error, String message) {
        this.status = error.value();
        this.message = message;
        this.error = error;
        this.timestamp = LocalDateTime.now().toString();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getError() {
        return error;
    }

    public String getTimestamp() {
        return timestamp;
    }
}