package com.example.claytoncodingassessment.service;

import com.example.claytoncodingassessment.model.entities.Task;

import java.util.List;
import java.util.Optional;

/**
 * This service implements the creation and retrieval of Tasks.
 */
public interface TaskService {

    /**
     * Create a task with title field in the body.
     *
     * @param task
     * @return
     */
    Task createTask(Task task);

    /**
     * Returns all tasks from the database.
     *
     * @return
     */
    List<Task> getAllTasks();

    /**
     * Returns one task found by its specific id.
     *
     * @param id
     * @return
     */
    Optional<Task> findTaskById(Long id);
}
