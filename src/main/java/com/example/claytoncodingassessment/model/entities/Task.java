package com.example.claytoncodingassessment.model.entities;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

/**
 * I assume there is an entity task, which will have one-to-one relationship with TaskExecutionReport.
 * The report will track the task progress.
 */
@Table(name = "task")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Long id;
    @NonNull
    public String title;
    @OneToOne(mappedBy = "taskId", fetch = FetchType.LAZY)
    private TaskExecutionReport taskExecutionReport;

    @JsonManagedReference
    public TaskExecutionReport getTaskExecutionReport() {
        return taskExecutionReport;
    }

}
