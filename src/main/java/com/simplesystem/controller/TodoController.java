package com.simplesystem.controller;

import com.simplesystem.exception.CannotUpdateStatusException;
import com.simplesystem.exception.PastDueTodoUpdateException;
import com.simplesystem.exception.TodoNotFoundException;
import com.simplesystem.model.TodoData;
import com.simplesystem.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/todo/")
public class TodoController {

    @Autowired
    TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoData> createTodo(@RequestBody TodoData todoData) throws CannotUpdateStatusException, HttpMessageNotReadableException {
        return new ResponseEntity<>(todoService.addTodo(todoData), HttpStatus.CREATED);
    }

    @PatchMapping("{id}")
    public ResponseEntity<TodoData> updateTodo(@PathVariable("id") UUID id, @RequestBody Map<Object, Object> fields)
            throws PastDueTodoUpdateException, TodoNotFoundException {
        return new ResponseEntity<>(todoService.updateTodo(id, fields), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TodoData>> getNotDoneTodo() {
        return new ResponseEntity<>(todoService.retrieveNotDoneTodo(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoData> getTodo(@PathVariable("id") UUID id) throws TodoNotFoundException {
        return new ResponseEntity<>(todoService.getTodo(id), HttpStatus.OK);
    }
}
