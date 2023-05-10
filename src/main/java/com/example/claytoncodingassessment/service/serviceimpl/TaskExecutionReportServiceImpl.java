package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.repository.TaskExecutionReportRepo;
import com.example.claytoncodingassessment.service.TaskExecutionReportService;
import com.example.claytoncodingassessment.service.TaskService;
import com.example.claytoncodingassessment.service.exceptions.AlreadySuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskExecutionReportServiceImpl implements TaskExecutionReportService {

    @Autowired
    private TaskExecutionReportRepo taskExecutionReportRepo;

    @Autowired
    private TaskService taskService;

    @Override
    public TaskExecutionReport createTaskExecutionReport(Long taskId, TaskExecutionReport taskExecutionReport) throws NoSuchTaskException, AlreadySuchTaskException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed", new Throwable());
        } else if (taskService.findTaskById(taskId).get().getTaskExecutionReport() != null) {
            throw new AlreadySuchTaskException("A TaskReport for task with id " + taskId + " already exists.", new Throwable());
        } else {
            taskExecutionReport.setTaskId(taskService.findTaskById(taskId).isPresent() ? taskService.findTaskById(taskId).get() : null);   // Setting the foreign key.
            taskExecutionReport.setStartDateTime(LocalDateTime.now());
            return taskExecutionReportRepo.save(taskExecutionReport);
        }
    }

    @Override
    public List<TaskExecutionReport> getAllTaskExecutionReports() {
        return new ArrayList<>(taskExecutionReportRepo.findAll());
    }

    @Override
    public TaskExecutionReport getTaskExecutionReport(Long taskId, Long taskReportId) throws NoSuchTaskException, NoSuchTaskReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed.", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        if (!Objects.equals(task.getTaskExecutionReport().getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId, new Throwable());
        }
        return taskExecutionReportRepo.findById(taskReportId).get();
    }

    @Override
    public TaskExecutionReport updateTaskExecutionReport(Long taskId, Long taskReportId, TaskExecutionReport taskExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed.", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        if (!Objects.equals(task.getTaskExecutionReport().getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId, new Throwable());
        }
        TaskExecutionReport taskReport = task.getTaskExecutionReport();
        taskReport.setErrorMessage(taskExecutionReport.getErrorMessage());
        return taskExecutionReportRepo.save(taskReport);
    }

    @Override
    public void deleteTaskExecutionReport(Long taskId, Long taskReportId) throws NoSuchTaskException, NoSuchTaskReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed.", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        if (!Objects.equals(task.getTaskExecutionReport().getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId, new Throwable());
        }
        taskExecutionReportRepo.deleteById(taskReportId);
    }

    // CRUD Support methods.
    public void updateAttributes(TaskExecutionReport taskExecutionReport) {
        taskExecutionReport.setStatus(updateStatus(taskExecutionReport));
        updateTime(taskExecutionReport);
        taskExecutionReportRepo.save(taskExecutionReport);
    }

    private Status updateStatus(TaskExecutionReport taskExecutionReport) {
        List<TaskStepExecutionReport> listOfSteps = new ArrayList<>(taskExecutionReport.getTaskStepExecutionReports());
        if (listOfSteps.isEmpty()) {
            return null;
        }
        for (TaskStepExecutionReport s : listOfSteps) {
            if (s.getStatus() == Status.FAILURE) {
                return Status.FAILURE;
            }
        }
        for (TaskStepExecutionReport s : listOfSteps) {
            if (s.getStatus() == Status.RUNNING) {
                return Status.RUNNING;
            }
        }
        return Status.SUCCESS;
    }

    private void updateTime(TaskExecutionReport taskExecutionReport) {
        Long exeTime = 0L;
        List<TaskStepExecutionReport> listOfSteps = new ArrayList<>(taskExecutionReport.getTaskStepExecutionReports());
        if (taskExecutionReport.getStatus() == Status.SUCCESS) {
            taskExecutionReport.setEndDateTime(LocalDateTime.now());
            for (TaskStepExecutionReport t : listOfSteps) {
                exeTime += t.getExecutionTimeSeconds();
            }
            taskExecutionReport.setExecutionTimeSeconds(exeTime);
        } else {
            taskExecutionReport.setExecutionTimeSeconds(null);
        }
    }

    @Override
    public List<TaskExecutionReport> getAllTaskExecutionReportsByStatus(Status status) {
        return taskExecutionReportRepo.findAll()
                .stream()
                .filter(t -> t.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskExecutionReport> getAllTaskExecutionReportsOrderedByExeTime() {
        return taskExecutionReportRepo.findAll()
                .stream()
                .filter(t -> t.getExecutionTimeSeconds() != null)
                .sorted(Comparator.comparing(TaskExecutionReport::getExecutionTimeSeconds))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskExecutionReport> findTaskReportById(Long id) {
        return taskExecutionReportRepo.findById(id);
    }
}

