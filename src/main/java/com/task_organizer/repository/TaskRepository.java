package com.task_organizer.repository;

import com.task_organizer.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

        @Query("FROM Task WHERE createdBy = ?1 OR assignee = ?1")
        List<Task> findTasksForLoggedInUser(String loggedInUser);

        @Query("FROM Task WHERE id=?1 AND createdBy = ?2")
        void deleteTaskById(Long taskId , String loggedInUser);
}
