package com.example.claytoncodingassessment.model.entities;

import com.example.claytoncodingassessment.model.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Table(name = "TaskExecutionReport")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class TaskExecutionReport {

    @Id                 // Tells the database that id will be unique for each TaskExecutionReport.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="taskReport_id", nullable = false)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable = false)
    @JsonBackReference
    private Task taskId;
    @Column
    private LocalDateTime startDateTime;
    @Column
    private LocalDateTime endDateTime;
    @Column
    private Long executionTimeSeconds;
    @Column
    private String errorMessage;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy="taskExecutionId", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)     // Tells the database that one task can have many steps.
    private Set<TaskStepExecutionReport> taskStepExecutionReports;

    @JsonManagedReference
    public Set<TaskStepExecutionReport> getTaskStepExecutionReports() {
        return taskStepExecutionReports;
    }

}
