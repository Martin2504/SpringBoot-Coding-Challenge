package com.example.claytoncodingassessment.repository;

import com.example.claytoncodingassessment.model.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepo extends JpaRepository<Task, Long> {
}
