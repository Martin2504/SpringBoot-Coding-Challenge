package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.repository.TaskStepExecutionReportRepo;
import com.example.claytoncodingassessment.service.TaskExecutionReportService;
import com.example.claytoncodingassessment.service.TaskService;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest
class TaskStepExecutionReportServiceImplTest {

    @Mock
    private TaskStepExecutionReportRepo taskStepExecutionReportRepo;
    @Mock
    private TaskExecutionReportService taskExecutionReportService;
    @Mock
    private TaskService taskServiceMock;
    @Mock
    private TaskExecutionReportServiceImpl taskExecutionReportServiceImpl;
    @InjectMocks
    private TaskStepExecutionReportServiceImpl taskStepExecutionReportServiceImpl;

    @Test
    @DisplayName("Creating a single StepReport.")
    void createTaskStepExecutionReport() throws Exception {

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(TaskExecutionReport.builder()
                        .id(1L)
                        .build())
                .build()));

        TaskStepExecutionReport taskStepExecutionReportToSave = TaskStepExecutionReport.builder()
                        .stepName("Test Step").status(Status.RUNNING).errorMessage("Test Error Message").build();

        when(taskExecutionReportService.findTaskReportById(any())).thenReturn(
                Optional.of(TaskExecutionReport.builder().build()));

        when(taskStepExecutionReportRepo.save(taskStepExecutionReportToSave)).thenReturn(taskStepExecutionReportToSave);

        TaskStepExecutionReport taskStepExecutionReportActual = taskStepExecutionReportServiceImpl.createTaskStepExecutionReport(
                1L, 1L, taskStepExecutionReportToSave);

        assertThat(taskStepExecutionReportActual).usingRecursiveComparison().isEqualTo(taskStepExecutionReportToSave);
    }

    @Test
    @DisplayName("Testing all StepReports are returned.")
    void getAllTaskStepExecutionReports() {

        TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                .taskId(
                        Task.builder()
                                .id(1L)
                                .title("Mandatory Task")
                                .build()
                ).id(1L).build();

        List<TaskStepExecutionReport> stepReportArrayList = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            TaskStepExecutionReport taskStepExecutionReport = TaskStepExecutionReport.builder()
                    .taskExecutionId(taskExecutionReport)
                    .id(i + 1L).build();
            stepReportArrayList.add(taskStepExecutionReport);
        }

        when(taskStepExecutionReportRepo.findAll()).thenReturn(stepReportArrayList);

        List<TaskStepExecutionReport> StepExecutionReportList = taskStepExecutionReportServiceImpl.
                getAllTaskStepExecutionReports();

        assertEquals(4, StepExecutionReportList.size());
    }

                // ToDo check why this doesn't work
    @Test
    @DisplayName("Testing specific StepReport is returned.")
    void getTaskStepExecutionReport() throws Exception {

        TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                .taskId(
                        Task.builder()
                                .id(1L)
                                .title("Mandatory Task")
                                .build()
                ).id(1L).build();

        Set<TaskStepExecutionReport> stepReportSet = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            TaskStepExecutionReport taskStepExecutionReport = TaskStepExecutionReport.builder()
                    .taskExecutionId(taskExecutionReport)
                    .id(i + 1L).build();
            stepReportSet.add(taskStepExecutionReport);
        }

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(TaskExecutionReport.builder()
                        .id(1L)
                        .taskStepExecutionReports(stepReportSet)
                        .build())
                .build()));

        List<TaskStepExecutionReport> setToList = new ArrayList<>(stepReportSet);

        List<TaskStepExecutionReport> taskSteplist =  setToList.stream().filter(t -> t.getId() == 3L).toList();

        TaskStepExecutionReport taskStepExecutionReportActual = taskStepExecutionReportServiceImpl.
                getTaskStepExecutionReport(1L, 1L, 3L);

        assertThat(taskStepExecutionReportActual).usingRecursiveComparison().isEqualTo(taskSteplist.get(0));

    }

    @Test
    @DisplayName("Testing StepReport is updated.")
    void updateTaskStepExecutionReport() throws Exception {

        TaskExecutionReport taskExecutionReportRequired = TaskExecutionReport.builder()
                .id(1L)
                .taskStepExecutionReports(Set.of(TaskStepExecutionReport.builder()
                        .id(1L).status(Status.RUNNING).build()))
                .build();

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .taskExecutionReport(taskExecutionReportRequired)
                .build()));

        TaskStepExecutionReport taskStepExecutionReportToSave = TaskStepExecutionReport.builder()
                .id(1L).status(Status.FAILURE).build();

        when(taskExecutionReportService.findTaskReportById(any())).thenReturn(Optional.of(taskExecutionReportRequired));

        when(taskStepExecutionReportRepo.save(any())).thenReturn(taskStepExecutionReportToSave);

        TaskStepExecutionReport taskStepExecutionReportActual = taskStepExecutionReportServiceImpl.
                updateTaskStepExecutionReport(1L, 1L, 1L, taskStepExecutionReportToSave);

        assertThat(taskStepExecutionReportActual).usingRecursiveComparison().isEqualTo(taskStepExecutionReportToSave);
    }

    @Test
    @DisplayName("Test the delete method throws NoSuchTaskException")
    void deleteTaskStepExecutionReportThrowsNoSuchTaskException() {
        assertThrows(NoSuchTaskException.class, ()->taskStepExecutionReportServiceImpl.deleteTaskStepExecutionReport(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Test the delete method throws NoSuchTaskException")
    void deleteTaskStepExecutionReportThrowsNoSuchTaskReportException() {

        when(taskServiceMock.findTaskById(1L)).thenReturn(Optional.of(Task.builder()
                .title("test Task")
                .build()));

        assertThrows(NoSuchTaskReportException.class, ()->taskStepExecutionReportServiceImpl.deleteTaskStepExecutionReport(1L, 1L, 1L));
    }

    @Test
    @DisplayName("Testing StepReport attributes are updated if status us SUCCESS.")
    void checkForSuccess() {

        TaskStepExecutionReport taskStepExecutionReport = TaskStepExecutionReport.builder()
                .startDateTime(LocalDateTime.now())
                .status(Status.SUCCESS)
                .build();

        taskStepExecutionReportServiceImpl.checkForSuccess(taskStepExecutionReport);

        assertNotNull(taskStepExecutionReport.getEndDateTime());
        assertNotNull(taskStepExecutionReport.getExecutionTimeSeconds());

        TaskStepExecutionReport taskStepExecutionReport1 = TaskStepExecutionReport.builder()
                .startDateTime(LocalDateTime.now())
                .status(Status.FAILURE)
                .build();

        taskStepExecutionReportServiceImpl.checkForSuccess(taskStepExecutionReport1);

        assertNull(taskStepExecutionReport1.getEndDateTime());
        assertNull(taskStepExecutionReport1.getExecutionTimeSeconds());
    }

    @Test
    void getAllTaskStepExecutionReportsSortedByStartDateTime() throws Exception {

        Set<TaskStepExecutionReport> taskStepExecutionReportSet = new HashSet<>();

        for(int i = 0; i < 4; i++) {
            taskStepExecutionReportSet.add(TaskStepExecutionReport.builder()
                    .startDateTime(LocalDateTime.of(2023, 12 - i, 1, 1, 1))
                    .build());
        }

        TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                .taskStepExecutionReports(taskStepExecutionReportSet)
                .build();

        when(taskExecutionReportService.findTaskReportById(any())).thenReturn(Optional.of(taskExecutionReport));

        List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl
                .getAllTaskStepExecutionReportsSortedByStartDateTime(1L);

        for (int i = 0; i < taskStepExecutionReportList.size() - 1; i++) {
            int diff = taskStepExecutionReportList.get(i).getStartDateTime().compareTo(taskStepExecutionReportList.get(i + 1).getStartDateTime());
            assertTrue(diff < 0);
        }
    }

    @Test
    void getAllTaskStepExecutionReportsSortedByExeTime() throws Exception {

        Set<TaskStepExecutionReport> taskStepExecutionReportSet = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            TaskStepExecutionReport taskStepExecutionReport = TaskStepExecutionReport.builder()
                    .executionTimeSeconds(i * 10L)
                    .build();
            taskStepExecutionReportSet.add(taskStepExecutionReport);
        }

        TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                .taskStepExecutionReports(taskStepExecutionReportSet)
                .build();

        when(taskExecutionReportService.findTaskReportById(any())).thenReturn(Optional.of(taskExecutionReport));

        List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl
                .getAllTaskStepExecutionReportsSortedByExeTime(1L);

        for (int i = 0; i < taskStepExecutionReportList.size() - 1; i++) {
            assertTrue(taskStepExecutionReportList.get(i).getExecutionTimeSeconds() <
                    taskStepExecutionReportList.get(i + 1).getExecutionTimeSeconds());
        }
    }

    @Test
    void getAllTaskStepExecutionReportGivenTaskExecutionReport() throws Exception {

        Set<TaskStepExecutionReport> taskStepExecutionReportSet = new HashSet<>();

        for (int i = 0; i < 4; i++) {
            taskStepExecutionReportSet.add(TaskStepExecutionReport.builder()
                    .executionTimeSeconds(i * 10L)
                    .build());
        }

        TaskExecutionReport taskExecutionReport = TaskExecutionReport.builder()
                .taskStepExecutionReports(taskStepExecutionReportSet)
                .build();

        when(taskExecutionReportService.findTaskReportById(any())).thenReturn(Optional.of(taskExecutionReport));

        List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl
                .getAllTaskStepExecutionReportGivenTaskExecutionReport(1L);

        assertEquals(4, taskStepExecutionReportList.size());
    }
}