package com.simplesystem.exception;

public class CannotUpdateStatusException extends RuntimeException {
    public CannotUpdateStatusException() {
        super();
    }

    public CannotUpdateStatusException(String message) {
        super(message);
    }
}
