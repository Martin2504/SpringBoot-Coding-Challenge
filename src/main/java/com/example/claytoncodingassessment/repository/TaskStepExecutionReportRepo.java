package com.example.claytoncodingassessment.repository;


import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStepExecutionReportRepo extends JpaRepository<TaskStepExecutionReport, Long> {
}
