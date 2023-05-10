package com.example.claytoncodingassessment.model.entities;

import com.example.claytoncodingassessment.model.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "TaskStepExecutionReport")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class  TaskStepExecutionReport  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "taskReport_id")
    @JsonBackReference      // This tells us the TaskReport owns the StepReport.
    private TaskExecutionReport taskExecutionId;
    @Column
    private String stepName;
    @Column
    private LocalDateTime startDateTime;
    @Column
    private LocalDateTime endDateTime;
    @Column
    private Long executionTimeSeconds;
    @Column
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column
    private String errorMessage;

    @JsonBackReference
    public TaskExecutionReport getTaskExecutionId() {
        return taskExecutionId;
    }
}
