package com.simplesystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.util.NestedServletException;

@ControllerAdvice
public class TodoExceptionHandler {
    @ExceptionHandler({CannotUpdateStatusException.class, PastDueTodoUpdateException.class, TodoNotFoundException.class,
            NestedServletException.class, IllegalArgumentException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorResponse> handleTodoException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(errorResponse);
    };
}
