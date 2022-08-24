package com.simplesystem.exception;

public class PastDueTodoUpdateException extends RuntimeException {
    public PastDueTodoUpdateException() {
        super();
    }

    public PastDueTodoUpdateException(String message) {
        super(message);
    }
}
