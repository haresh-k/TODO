package com.simplesystem.exception;

public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException() {
        super();
    }

    public TodoNotFoundException(String message) {
        super(message);
    }
}
