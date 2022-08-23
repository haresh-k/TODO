package com.simplesystem.service;

import com.simplesystem.constants.TodoStatus;
import com.simplesystem.model.TodoData;
import com.simplesystem.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    TodoRepository todoRepository;

    @Override
    public TodoData addTodo(TodoData todoData) throws IllegalArgumentException {
        if(todoData.getStatus().equals(TodoStatus.PAST_DUE)) {
            throw new IllegalArgumentException("Cannot mark status as " + TodoStatus.PAST_DUE);
        }
        TodoData savedData = todoRepository.save(todoData);
        return savedData;
    }

    @Override
    public List<TodoData> retrieveNotDoneTodo() {
        return todoRepository.findByStatus(TodoStatus.NOT_DONE);
    }

    @Override
    public TodoData getTodo(UUID id) throws IllegalArgumentException {
        Optional<TodoData> data = todoRepository.findById(id);
        if(!data.isPresent()) {
            throw new IllegalArgumentException("Data for " + id + " not available");
        }
        return todoRepository.findById(id).get();
    }

    @Override
    public TodoData updateTodo(UUID id, Map<Object, Object> fields) {
        Optional<TodoData> data = todoRepository.findById(id);
        if(data.isPresent()) {
            if(data.get().getStatus().equals(TodoStatus.PAST_DUE)) {
                throw new IllegalArgumentException("Data for " + id + " cannot be modified");
            }
            fields.forEach((k, v) -> {
                if (k.toString().equalsIgnoreCase("description")
                        || k.toString().equalsIgnoreCase("status")) {
                    Field field = ReflectionUtils.findField(TodoData.class, k.toString());
                    field.setAccessible(true);
                    if(field.getType().isEnum()) {
                        TodoStatus status = TodoStatus.valueOf(v.toString());
                        ReflectionUtils.setField(field, data.get(), status);
                        Field doneDateField = ReflectionUtils.findField(TodoData.class, "doneDate");
                        doneDateField.setAccessible(true);
                        if(status.equals(TodoStatus.DONE)) {
                            ReflectionUtils.setField(doneDateField, data.get(), LocalDateTime.now());
                        } else {
                            ReflectionUtils.setField(doneDateField, data.get(), null);
                        }
                    } else {
                        ReflectionUtils.setField(field, data.get(), v);
                    }
                }
            });
        }
        return todoRepository.save(data.get());
    }
}
