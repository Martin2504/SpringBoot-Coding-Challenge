package com.example.claytoncodingassessment.repository;

import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskExecutionReportRepo extends JpaRepository<TaskExecutionReport, Long> {
}
