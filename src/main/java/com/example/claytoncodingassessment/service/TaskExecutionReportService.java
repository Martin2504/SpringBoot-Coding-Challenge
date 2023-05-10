package com.example.claytoncodingassessment.service;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.service.exceptions.AlreadySuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;

import java.util.List;
import java.util.Optional;

/**
 * This service implements the CRUD operations for the TaskExecutionReport resource.
 */
public interface TaskExecutionReportService {

    /**
     * Gets the taskId and creates a corresponding TaskReport.
     * Persists a new resource to the database with errorMessagae in the body.
     *
     * @param taskId
     * @param taskExecutionReport
     * @return
     * @throws NoSuchTaskException
     * @throws AlreadySuchTaskException
     */
    TaskExecutionReport createTaskExecutionReport(Long taskId, TaskExecutionReport taskExecutionReport) throws NoSuchTaskException, AlreadySuchTaskException;

    /**
     * Returns all TaskExecutionReports from the database.
     * @return
     */
    List<TaskExecutionReport> getAllTaskExecutionReports();

    /**
     * Gets the taskId and taskReportId and then returns the corresponding TaskReport.
     * @param taskId
     * @param taskReportId
     * @return
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     */
    TaskExecutionReport getTaskExecutionReport(Long taskId, Long taskReportId) throws NoSuchTaskException, NoSuchTaskReportException;

    /**
     * Updates an existing TaskReport in the database.
     * @param taskId
     * @param taskReportId
     * @param taskExecutionReport
     * @return
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     */
    TaskExecutionReport updateTaskExecutionReport(Long taskId, Long taskReportId, TaskExecutionReport taskExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException;

    /**
     * Deletes an existing TaskReport from the database.
     * @param taskId
     * @param taskReportId
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     */
    void deleteTaskExecutionReport(Long taskId, Long taskReportId) throws NoSuchTaskException, NoSuchTaskReportException;

    /**
     * Returns all TaskReports with given status.
     * @param status
     * @return
     */
    List<TaskExecutionReport> getAllTaskExecutionReportsByStatus(Status status);

    /**
     * Returns all TaskReports ordered by execution time (fastest to slowest).
     * @return
     */
    List<TaskExecutionReport> getAllTaskExecutionReportsOrderedByExeTime();

    /**
     * Returns a TaskReport given its id.
     * @param id
     * @return
     */
    Optional<TaskExecutionReport> findTaskReportById(Long id);
}
