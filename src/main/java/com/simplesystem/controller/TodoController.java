package com.simplesystem.controller;

import com.simplesystem.model.TodoData;
import com.simplesystem.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/todo/")
public class TodoController {

    @Autowired
    TodoService todoService;

    @PostMapping
    public ResponseEntity<TodoData> createTodo(@RequestBody TodoData todoData) {
        try {
            return new ResponseEntity<>(todoService.addTodo(todoData), HttpStatus.CREATED);
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    @PatchMapping("{id}")
    public ResponseEntity<TodoData> updateTodo(@PathVariable("id") UUID id, @RequestBody Map<Object, Object> fields) {
        try {
            TodoData data = todoService.getTodo(id);
            return new ResponseEntity<>(todoService.updateTodo(id, fields), HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.toString());
        }
    }

    @GetMapping
    public ResponseEntity<List<TodoData>> getNotDoneTodo() {
        return new ResponseEntity<>(todoService.retrieveNotDoneTodo(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<TodoData> getTodo(@PathVariable("id") UUID id) {
        try {
            TodoData data = todoService.getTodo(id);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch(IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "UUID not found", e);
        }
    }
}
