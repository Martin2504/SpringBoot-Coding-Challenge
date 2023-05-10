package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.repository.TaskExecutionReportRepo;
import com.example.claytoncodingassessment.service.TaskService;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class TaskExecutionReportServiceImplTest {

    @Mock
    private TaskExecutionReportRepo taskExecutionReportRepoMock;

    @Mock
    private TaskService taskServiceMock;

    @InjectMocks
    private TaskExecutionReportServiceImpl taskExecutionReportServiceimpl;

    @Test
    @DisplayName("Creating a single TaskReport.")
    void createTaskExecutionReport() throws Exception {
        // Mock new task
        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .build()));

        TaskExecutionReport taskExecutionReportToSave = TaskExecutionReport.builder()
                .errorMessage("This is a test.").build();

        when(taskExecutionReportRepoMock.save(taskExecutionReportToSave)).thenReturn(taskExecutionReportToSave);

        TaskExecutionReport taskExecutionReportActual = taskExecutionReportServiceimpl.
                createTaskExecutionReport(1L ,taskExecutionReportToSave);

        assertThat(taskExecutionReportActual).usingRecursiveComparison().isEqualTo(taskExecutionReportToSave);

    }

    @Test
    @DisplayName("Testing all TaskReports are returned.")
    void getAllTaskExecutionReports() {

        List<TaskExecutionReport> taskReportArrayList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                    .taskId(
                            Task.builder()
                                    .id(i + 1L)
                                    .title("Mandatory Task")
                                    .build()
                    ).id(i + 1L).build();
            taskReportArrayList.add(taskExecutionReport);
        }

        when(taskExecutionReportRepoMock.findAll()).thenReturn(taskReportArrayList);

        List<TaskExecutionReport> TaskExecutionReportList = taskExecutionReportServiceimpl.
                getAllTaskExecutionReports();

        assertEquals(4, TaskExecutionReportList.size());
    }

    @Test
    @DisplayName("Testing specific TaskReport is returned.")
    void getTaskExecutionReport() throws Exception {

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(TaskExecutionReport.builder()
                        .id(1L)
                        .build())
                .build()));

        TaskExecutionReport taskExecutionReportToReturn = TaskExecutionReport.builder()
                .errorMessage("This is a test.").build();

        when(taskExecutionReportRepoMock.findById(1L)).thenReturn(Optional.ofNullable(taskExecutionReportToReturn));

        TaskExecutionReport taskExecutionReportActual = taskExecutionReportServiceimpl.
                getTaskExecutionReport(1L, 1L);

        assertThat(taskExecutionReportActual).usingRecursiveComparison().isEqualTo(taskExecutionReportToReturn);
    }

    @Test
    @DisplayName("Testing TaskReport is updated.")
    void updateTaskExecutionReport() throws Exception {

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(TaskExecutionReport.builder()
                        .id(1L)
                        .build())
                .build()));

        TaskExecutionReport taskExecutionReportToSave = TaskExecutionReport.builder()
                .errorMessage("This is a test.").build();

        when(taskExecutionReportRepoMock.save(any())).thenReturn(taskExecutionReportToSave);

        TaskExecutionReport taskExecutionReportActual = taskExecutionReportServiceimpl.
                updateTaskExecutionReport(1L, 1L, taskExecutionReportToSave);

        assertThat(taskExecutionReportActual).usingRecursiveComparison().isEqualTo(taskExecutionReportToSave);
    }

    @Test
    @DisplayName("Testing delete method throws correct exception.")
    void deleteTaskExecutionReport() {
        TaskExecutionReport taskExecutionReportToBeDeleted = TaskExecutionReport.builder()
                        .id(10L).build();

        when(taskServiceMock.findTaskById(any())).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(taskExecutionReportToBeDeleted)
                .build()));

        assertThrows(NoSuchTaskReportException.class, () -> taskExecutionReportServiceimpl.
                deleteTaskExecutionReport(10L, 9L));
    }

    @Test
    @DisplayName("Testing that a TaskReports attributes get updated when a stepReport's status is SUCCESS")
    void updateAttributes() {

        TaskStepExecutionReport taskStepExecutionReportSUCCESS = TaskStepExecutionReport.builder()
                .executionTimeSeconds(100L)
                .status(Status.SUCCESS)
                .build();

        TaskExecutionReport taskExecutionReportToPass = TaskExecutionReport.builder()
                .taskStepExecutionReports(Set.of(taskStepExecutionReportSUCCESS))
                .build();

        taskExecutionReportServiceimpl.updateAttributes(taskExecutionReportToPass);

        assertThat(taskExecutionReportToPass.getStatus()).isEqualTo(Status.SUCCESS);
        assertNotNull(taskExecutionReportToPass.getEndDateTime());
        assertThat(taskExecutionReportToPass.getExecutionTimeSeconds()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Testing all TaskReports with given status are returned.")
    void getAllTaskExecutionReportsByStatus() {

        List<TaskExecutionReport> taskReportArrayList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                    .taskId(
                            Task.builder()
                                    .id(i + 1L)
                                    .title("Mandatory Task")
                                    .build()
                    ).status(Status.SUCCESS)
                    .id(i + 1L).build();
            taskReportArrayList.add(taskExecutionReport);
        }
        for (int i = 3; i < 5; i++) {
            TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                    .taskId(
                            Task.builder()
                                    .id(i + 1L)
                                    .title("Mandatory Task")
                                    .build()
                    ).status(Status.FAILURE)
                    .id(i + 1L).build();
            taskReportArrayList.add(taskExecutionReport);
        }

        when(taskExecutionReportRepoMock.findAll()).thenReturn(taskReportArrayList);

        assertEquals(3, taskExecutionReportServiceimpl.
                getAllTaskExecutionReportsByStatus(Status.SUCCESS).size());
        assertEquals(2, taskExecutionReportServiceimpl.
                getAllTaskExecutionReportsByStatus(Status.FAILURE).size());
        assertEquals(0, taskExecutionReportServiceimpl.
                getAllTaskExecutionReportsByStatus(Status.RUNNING).size());
    }

    @Test
    @DisplayName("Testing TaskReports are returned ordered by ExecutionTime.")
    void getAllTaskExecutionReportsOrderedByExeTime() {

        List<TaskExecutionReport> taskReportArrayList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                    .taskId(
                            Task.builder()
                                    .id(i + 1L)
                                    .title("Mandatory Task")
                                    .build()
                    ).executionTimeSeconds(i * 10L)
                    .id(i + 1L).build();
            taskReportArrayList.add(taskExecutionReport);
        }

        when(taskExecutionReportRepoMock.findAll()).thenReturn(taskReportArrayList);

        taskExecutionReportServiceimpl.getAllTaskExecutionReportsOrderedByExeTime();

        for (int i = 0; i < taskReportArrayList.size() - 1; i++) {
            assertTrue(taskReportArrayList.get(i).getExecutionTimeSeconds()
                    < taskReportArrayList.get(i + 1).getExecutionTimeSeconds());
        }
    }

}