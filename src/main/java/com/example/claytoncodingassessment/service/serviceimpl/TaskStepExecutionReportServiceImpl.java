package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.repository.TaskStepExecutionReportRepo;
import com.example.claytoncodingassessment.service.TaskExecutionReportService;
import com.example.claytoncodingassessment.service.TaskService;
import com.example.claytoncodingassessment.service.TaskStepExecutionReportService;
import com.example.claytoncodingassessment.service.exceptions.NoSuchStepReportException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskStepExecutionReportServiceImpl implements TaskStepExecutionReportService {

    @Autowired
    private TaskStepExecutionReportRepo taskStepExecutionReportRepo;
    @Autowired
    private TaskExecutionReportService taskExecutionReportService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskExecutionReportServiceImpl taskExecutionReportServiceImpl;

    @Override
    public TaskStepExecutionReport createTaskStepExecutionReport(Long taskId, Long taskReportId, TaskStepExecutionReport taskStepExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        if (!Objects.equals(task.getTaskExecutionReport().getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId,  new Throwable());
        }
        taskStepExecutionReport.setTaskExecutionId(taskExecutionReportService.findTaskReportById(taskReportId).get());   // Setting the foreign key
        taskStepExecutionReport.setStartDateTime(LocalDateTime.now());
        checkForSuccess(taskStepExecutionReport);
        TaskStepExecutionReport createdStepReport = taskStepExecutionReportRepo.save(taskStepExecutionReport);
        taskExecutionReportServiceImpl.updateAttributes(createdStepReport.getTaskExecutionId());
        return createdStepReport;
    }

    @Override
    public List<TaskStepExecutionReport> getAllTaskStepExecutionReports() {
        return new ArrayList<>(taskStepExecutionReportRepo.findAll());
    }

    @Override
    public TaskStepExecutionReport getTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId) throws NoSuchTaskException, NoSuchTaskReportException, NoSuchStepReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        TaskExecutionReport taskReport = task.getTaskExecutionReport();
        if (!Objects.equals(taskReport.getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId, new Throwable());
        }
        Set<TaskStepExecutionReport> stepReportSet = taskReport.getTaskStepExecutionReports();
        for (TaskStepExecutionReport t : stepReportSet) {
            if (Objects.equals(t.getId(), stepReportId)) {
                return t;
            }
        }
        throw new NoSuchStepReportException("Non-existent stepReportId passed for TaskReport with id " + taskReportId, new Throwable());
    }

    @Override
    public TaskStepExecutionReport updateTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId,
                                                       TaskStepExecutionReport taskStepExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException, NoSuchStepReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        TaskExecutionReport taskReport = task.getTaskExecutionReport();
        if (taskReport == null || !Objects.equals(taskReport.getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id " + taskId, new Throwable());
        }
        Set<TaskStepExecutionReport> stepReportSet = taskReport.getTaskStepExecutionReports();
        for (TaskStepExecutionReport t : stepReportSet) {
            if (Objects.equals(t.getId(), stepReportId)) {
                taskStepExecutionReport.setId(t.getId());
                taskStepExecutionReport.setTaskExecutionId(taskExecutionReportService.findTaskReportById(taskReportId).get());
                taskStepExecutionReport.setStartDateTime(t.getStartDateTime());
                checkForSuccess(taskStepExecutionReport);
                taskStepExecutionReportRepo.save(taskStepExecutionReport);
                taskExecutionReportServiceImpl.updateAttributes(taskStepExecutionReport.getTaskExecutionId());
                return taskStepExecutionReport;
            }
        }
        throw new NoSuchStepReportException("Non-existent stepReportId passed for TaskReport with id " + taskReportId, new Throwable());
    }

    @Override
    public void deleteTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId) throws NoSuchTaskException, NoSuchTaskReportException, NoSuchStepReportException {
        if (taskService.findTaskById(taskId).isEmpty()) {
            throw new NoSuchTaskException("Non-existent taskId passed", new Throwable());
        }
        Task task = taskService.findTaskById(taskId).get();
        TaskExecutionReport taskReport = task.getTaskExecutionReport();
        if (taskReport == null || !Objects.equals(taskReport.getId(), taskReportId)) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed for Task with id" + taskId, new Throwable());
        }
        Set<TaskStepExecutionReport> stepReportSet = taskReport.getTaskStepExecutionReports();
        for (TaskStepExecutionReport t : stepReportSet) {
            if (Objects.equals(t.getId(), stepReportId)) {
                taskStepExecutionReportRepo.deleteById(stepReportId);
                TaskExecutionReport taskReportToUpdate = taskExecutionReportService.findTaskReportById(taskReportId).get();
                Set<TaskStepExecutionReport> steps = taskReportToUpdate.getTaskStepExecutionReports();
                Iterator<TaskStepExecutionReport> iterator = steps.iterator();
                while(iterator.hasNext()) {
                    TaskStepExecutionReport tser = iterator.next();
                    if (Objects.equals(tser.getId(), stepReportId)) {
                        iterator.remove();
                        break;
                    }
                }
                taskExecutionReportServiceImpl.updateAttributes(taskReportToUpdate);
            }
        }
        throw new NoSuchStepReportException("Non-existent stepReportId passed for TaskReport with id " + taskReportId, new Throwable());
    }

    // CRUD Support method.
    public void checkForSuccess(TaskStepExecutionReport taskStepExecutionReport) {
        if (taskStepExecutionReport.getStatus() == Status.SUCCESS) {
            taskStepExecutionReport.setEndDateTime(LocalDateTime.now());
            taskStepExecutionReport.setExecutionTimeSeconds(ChronoUnit.SECONDS.between(
                    taskStepExecutionReport.getStartDateTime(), taskStepExecutionReport.getEndDateTime()
            ));
        } else {
            taskStepExecutionReport.setEndDateTime(null);
            taskStepExecutionReport.setExecutionTimeSeconds(null);
        }
    }

    @Override
    public List<TaskStepExecutionReport> getAllTaskStepExecutionReportsSortedByStartDateTime(Long taskReportId) throws NoSuchTaskReportException {
        if(taskExecutionReportService.findTaskReportById(taskReportId).isEmpty()) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed.", new Throwable());
        }
        return taskExecutionReportService.findTaskReportById(taskReportId).get().getTaskStepExecutionReports()
                .stream()
                .sorted(Comparator.comparing(TaskStepExecutionReport::getStartDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskStepExecutionReport> getAllTaskStepExecutionReportsSortedByExeTime(Long taskReportId) throws NoSuchTaskReportException {
        if(taskExecutionReportService.findTaskReportById(taskReportId).isEmpty()) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed.", new Throwable());
        }
        return taskExecutionReportService.findTaskReportById(taskReportId).get().getTaskStepExecutionReports()
                .stream()
                .filter(t -> t.getExecutionTimeSeconds() != null)
                .sorted(Comparator.comparing(TaskStepExecutionReport::getExecutionTimeSeconds))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TaskStepExecutionReport> getAllTaskStepExecutionReportGivenTaskExecutionReport(Long taskReportId) throws NoSuchTaskReportException {
        if(taskExecutionReportService.findTaskReportById(taskReportId).isEmpty()) {
            throw new NoSuchTaskReportException("Non-existent taskReportId passed.", new Throwable());
        }
        return new ArrayList<>(taskExecutionReportService.findTaskReportById(taskReportId).get().getTaskStepExecutionReports());
    }
}
