package com.simplesystem.service;

import com.simplesystem.model.TodoData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface TodoService {
    public TodoData addTodo(TodoData todoData);

    public List<TodoData> retrieveNotDoneTodo();

    public TodoData getTodo(UUID id);

    public TodoData updateTodo(UUID id, Map<Object, Object> fields);
}
