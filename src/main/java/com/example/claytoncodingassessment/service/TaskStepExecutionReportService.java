package com.example.claytoncodingassessment.service;

import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.service.exceptions.NoSuchStepReportException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;

import java.util.List;
import java.util.Optional;

/**
 * This service implements the CRUD operations for the TaskStepExecutionReport resource.
 */
public interface TaskStepExecutionReportService {

    /**
     * Gets the taskId and TaskReport and creates a corresponding StepReport.
     * Persists a new resource to the database with stepName, status and errorMessage in the body.
     * @param taskId
     * @param taskReportId
     * @param taskStepExecutionReport
     * @return
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     */
    TaskStepExecutionReport createTaskStepExecutionReport(Long taskId, Long taskReportId, TaskStepExecutionReport taskStepExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException;

    /**
     * Returns all TaskStepExecutionReports from the database.
     * @return
     */
    List<TaskStepExecutionReport> getAllTaskStepExecutionReports();

    /**
     * Gets the taskId, taskReportId and stepReportId and returns the corresponding StepReport.
      * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @return
     * @throws Exception
     */
    TaskStepExecutionReport getTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId) throws Exception;

    /**
     * Updates an existing StepReport in the database.
     * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @param taskStepExecutionReport
     * @return
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     * @throws NoSuchStepReportException
     */
    TaskStepExecutionReport updateTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId, TaskStepExecutionReport taskStepExecutionReport) throws NoSuchTaskException, NoSuchTaskReportException, NoSuchStepReportException;

    /**
     * Deletes an existing StepReport from the database.
     * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @throws NoSuchTaskException
     * @throws NoSuchTaskReportException
     * @throws NoSuchStepReportException
     */
    void  deleteTaskStepExecutionReport(Long taskId, Long taskReportId, Long stepReportId) throws NoSuchTaskException, NoSuchTaskReportException, NoSuchStepReportException;

    /**
     * Returns all StepReports for a given TaskReport ordered by StartDateTime.
     * @param taskReportId
     * @return
     * @throws NoSuchTaskReportException
     */
    List<TaskStepExecutionReport> getAllTaskStepExecutionReportsSortedByStartDateTime(Long taskReportId) throws NoSuchTaskReportException;

    /**
     * Returns all StepReports for a given TaskReport ordered by execution time.
     * @param taskReportId
     * @return
     * @throws NoSuchTaskReportException
     */
    List<TaskStepExecutionReport> getAllTaskStepExecutionReportsSortedByExeTime(Long taskReportId) throws NoSuchTaskReportException;

    /**
     * Returns all StepReports for a given TaskReport unordered.
     * @param taskReportId
     * @return
     * @throws NoSuchTaskReportException
     */
    List<TaskStepExecutionReport> getAllTaskStepExecutionReportGivenTaskExecutionReport(Long taskReportId) throws NoSuchTaskReportException;
}
