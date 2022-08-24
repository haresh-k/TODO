package com.simplesystem.service;

import com.simplesystem.constants.TodoStatus;
import com.simplesystem.exception.CannotUpdateStatusException;
import com.simplesystem.exception.PastDueTodoUpdateException;
import com.simplesystem.exception.TodoNotFoundException;
import com.simplesystem.model.TodoData;
import com.simplesystem.repository.TodoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class TodoServiceUnitTest {
    @Mock
    TodoRepository todoRepository;

    @InjectMocks
    TodoServiceImpl todoService;

    @BeforeAll
    static void setup() {
    }

    @Test
    void test01_createValidTodo() {
        UUID validNotDoneUuid = UUID.randomUUID();
        TodoData validDone = new TodoData();
        validDone.setUuid(validNotDoneUuid);
        validDone.setStatus(TodoStatus.NOT_DONE);
        validDone.setDescription("Not done todo");
        when(todoRepository.save(validDone)).thenReturn(validDone);
        TodoData savedData = todoService.addTodo(validDone);
        assertEquals(validDone.getUuid(), savedData.getUuid());
    }

    @Test
    void test02_createInValidTodo() {
        UUID validPastDueUuid = UUID.randomUUID();
        TodoData invalidPastDue = new TodoData();
        invalidPastDue.setUuid(validPastDueUuid);
        invalidPastDue.setStatus(TodoStatus.PAST_DUE);
        invalidPastDue.setDescription("Past due todo");
        assertThrows(CannotUpdateStatusException.class, () -> todoService.addTodo(invalidPastDue));
    }

    @Test
    void test03_updateDescriptionValidTodo() {
        UUID validUuid = UUID.randomUUID();
        String initialDescription = "Before Description Update";
        String finalDescription = "After Description Update";

        Map<Object, Object> updateMap = new HashMap<>();
        updateMap.put("description", finalDescription);

        TodoData validDone = new TodoData();
        validDone.setUuid(validUuid);
        validDone.setStatus(TodoStatus.DONE);
        validDone.setDescription(initialDescription);
        when(todoRepository.findById(validUuid)).thenReturn(Optional.of(validDone));
        validDone.setDescription(finalDescription);
        when(todoRepository.save(validDone)).thenReturn(validDone);

        TodoData updatedData = todoService.updateTodo(validUuid, updateMap);
        assertEquals(finalDescription, updatedData.getDescription());
    }

    @Test
    void test04_updateStatusValidTodo() {
        UUID validUuid = UUID.randomUUID();
        TodoStatus intialStatus = TodoStatus.NOT_DONE;
        TodoStatus finalStatus = TodoStatus.DONE;

        Map<Object, Object> updateMap = new HashMap<>();
        updateMap.put("status", finalStatus);

        TodoData validDone = new TodoData();
        validDone.setUuid(validUuid);
        validDone.setStatus(intialStatus);
        validDone.setDescription("Check out the description");
        when(todoRepository.findById(validUuid)).thenReturn(Optional.of(validDone));
        validDone.setStatus(finalStatus);
        when(todoRepository.save(validDone)).thenReturn(validDone);

        TodoData updatedData = todoService.updateTodo(validUuid, updateMap);
        assertEquals(finalStatus, updatedData.getStatus());
    }

    @Test
    void test05_updateToInvalidStatus() {
        UUID validUuid = UUID.randomUUID();
        TodoStatus intialStatus = TodoStatus.NOT_DONE;
        TodoStatus finalStatus = TodoStatus.PAST_DUE;

        Map<Object, Object> updateMap = new HashMap<>();
        updateMap.put("status", finalStatus);

        TodoData validPastDue = new TodoData();
        validPastDue.setUuid(validUuid);
        validPastDue.setStatus(intialStatus);
        validPastDue.setDescription("Check out the description");
        when(todoRepository.findById(validUuid)).thenReturn(Optional.of(validPastDue));
        validPastDue.setStatus(finalStatus);
        assertThrows(PastDueTodoUpdateException.class, () -> todoService.updateTodo(validUuid, updateMap));
    }

    @Test
    void test06_getAllNotDoneItems() {
        List<TodoData> todoDataList = new ArrayList<>();
        UUID validNotDoneUuid = UUID.randomUUID();
        TodoData validNotDone = new TodoData();
        validNotDone.setUuid(validNotDoneUuid);
        validNotDone.setStatus(TodoStatus.NOT_DONE);
        validNotDone.setDescription("Not done valid todo");
        todoDataList.add(validNotDone);
        when(todoRepository.findByStatus(TodoStatus.NOT_DONE)).thenReturn(todoDataList);
        assertEquals(todoDataList.size(), todoService.retrieveNotDoneTodo().size());
    }

    @Test
    void test07_getValidTodo() {
        UUID validNotDoneUuid = UUID.randomUUID();
        TodoData validNotDone = new TodoData();
        validNotDone.setUuid(validNotDoneUuid);
        validNotDone.setStatus(TodoStatus.NOT_DONE);
        validNotDone.setDescription("Not done valid todo");
        when(todoRepository.findById(validNotDoneUuid)).thenReturn(Optional.of(validNotDone));

        UUID validDoneUuid = UUID.randomUUID();
        TodoData validDone = new TodoData();
        validDone.setUuid(validDoneUuid);
        validDone.setStatus(TodoStatus.DONE);
        validDone.setDescription("Done valid todo");
        when(todoRepository.findById(validDoneUuid)).thenReturn(Optional.of(validDone));

        TodoData todoData = todoService.getTodo(validNotDoneUuid);
        assertEquals(TodoStatus.NOT_DONE, todoData.getStatus());

        todoData = todoService.getTodo(validDoneUuid);
        assertEquals(TodoStatus.DONE, todoData.getStatus());
    }

    @Test
    void test08_getInvalidTodo() {
        UUID invalidUuid = UUID.randomUUID();
        when(todoRepository.findById(invalidUuid)).thenReturn(Optional.empty());
        assertThrows(TodoNotFoundException.class, () -> todoService.getTodo(invalidUuid));
    }
}
