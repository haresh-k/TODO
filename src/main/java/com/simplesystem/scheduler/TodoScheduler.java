package com.simplesystem.scheduler;

import com.simplesystem.constants.TodoStatus;
import com.simplesystem.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class TodoScheduler {

    @Autowired
    TodoRepository todoRepository;

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void scheduleDueTasks() {
        todoRepository.updateDueTasks(TodoStatus.PAST_DUE, LocalDateTime.now());
    }
}
