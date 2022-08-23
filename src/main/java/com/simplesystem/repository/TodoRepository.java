package com.simplesystem.repository;

import com.simplesystem.constants.TodoStatus;
import com.simplesystem.model.TodoData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<TodoData, UUID> {
    List<TodoData> findByStatus(TodoStatus status);

    @Modifying
    @Query("UPDATE TodoData t set t.status = :status WHERE t.status != 'DONE' and t.dueDate < :currentTime")
    void updateDueTasks(@Param("status")TodoStatus status, @Param("currentTime")LocalDateTime currentTime);
}
