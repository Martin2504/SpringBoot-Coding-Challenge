package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.repository.TaskRepo;
import com.example.claytoncodingassessment.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepo taskRepo;

    @Override
    public Task createTask(Task task) {
        return taskRepo.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    @Override
    public Optional<Task> findTaskById(Long id) {
        return taskRepo.findById(id);
    }

}