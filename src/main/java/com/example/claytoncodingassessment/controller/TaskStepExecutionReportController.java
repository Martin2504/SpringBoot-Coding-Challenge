package com.example.claytoncodingassessment.controller;

import com.example.claytoncodingassessment.model.entities.TaskStepExecutionReport;
import com.example.claytoncodingassessment.service.exceptions.NoSuchStepReportException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import com.example.claytoncodingassessment.service.serviceimpl.TaskStepExecutionReportServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Tag(name ="TaskStepExecutionReport", description = "CRUD operations for TaskStepExecutionReport resource.")
public class TaskStepExecutionReportController {

    @Autowired
    private TaskStepExecutionReportServiceImpl taskStepExecutionReportServiceImpl;

    /**
     * Create a StepReport.
     *
     * @param taskId
     * @param taskReportId
     * @param taskStepExecutionReport
     * @return
     * @param <T>
     */
    @Operation(summary = "Create a new StepReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("StepReport with generated id of 1, has been added.")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @PostMapping("/tasks/{taskId}/taskReports/{taskReportId}/stepReports")
    public <T> T  setTaskStepExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId,
                                             @RequestBody TaskStepExecutionReport taskStepExecutionReport) {
        try {
            taskStepExecutionReportServiceImpl.createTaskStepExecutionReport(taskId, taskReportId, taskStepExecutionReport);
            return (T) new ResponseEntity<String>("StepReport with generated id of " + taskStepExecutionReport.getId() + ", has been added.",
                    HttpStatus.CREATED);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get all StepReports.
     *
     * @return
     */
    @Operation(summary = "Get all StepReports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("""
                                            [
                                            	{
                                            		"id": 1,
                                            		"stepName": "step2",
                                            		"startDateTime": "2023-05-09T23:55:33.10757",
                                            		"endDateTime": "2023-05-09T23:55:33.10757",
                                            		"executionTimeSeconds": 0,
                                            		"status": "SUCCESS",
                                            		"errorMessage": "N/A"
                                            	}
                                            ]
                                            """)
                            }
                    )
            }),
    })
    @GetMapping("/tasks/taskReports/stepReports")
    public ResponseEntity<List<TaskStepExecutionReport>> getAllTaskStepExecutionReports() {
        return new ResponseEntity<>(taskStepExecutionReportServiceImpl.getAllTaskStepExecutionReports(),
                HttpStatus.OK);
    }

    /**
     * Get a specific StepReport.
     *
     * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @return
     * @param <T>
     */
    @Operation(summary = "Get a specific StepReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("""
                                            {
                                            	"id": 1,
                                            	"stepName": "step2",
                                            	"startDateTime": "2023-05-09T23:55:33.10757",
                                            	"endDateTime": "2023-05-09T23:55:33.10757",
                                            	"executionTimeSeconds": 0,
                                            	"status": "SUCCESS",
                                            	"errorMessage": "N/A"
                                            }
                                            """)
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @GetMapping("/tasks/{taskId}/taskReports/{taskReportId}/stepReports/{stepReportId}")
    public <T> T getTaskStepExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId,
                                                        @PathVariable Long stepReportId) {
        try {
            TaskStepExecutionReport taskStepExecutionReport = taskStepExecutionReportServiceImpl.getTaskStepExecutionReport(taskId, taskReportId, stepReportId);
            return (T) new ResponseEntity<>(taskStepExecutionReport, HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchStepReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Update a specific StepReport.
     *
     * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @param taskStepExecutionReport
     * @return
     * @param <T>
     */
    @Operation(summary = "Update a specific StepReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Updated")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @PutMapping("/tasks/{taskId}/taskReports/{taskReportId}/stepReports/{stepReportId}")              // e.g. endpoint: ...
    public <T> T updateTaskStepExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId,
                                               @PathVariable Long stepReportId,
                                               @RequestBody TaskStepExecutionReport taskStepExecutionReport) {
        try {
            taskStepExecutionReportServiceImpl.updateTaskStepExecutionReport(taskId, taskReportId, stepReportId, taskStepExecutionReport);
            return (T) new ResponseEntity<String>("Updated", HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchStepReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a specific StepReport.
     *
     * @param taskId
     * @param taskReportId
     * @param stepReportId
     * @return
     * @param <T>
     */
    @Operation(summary = "Delete a specific StepReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Deleted")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @DeleteMapping("/tasks/{taskId}/taskReports/{taskReportId}/stepReports/{stepReportId}")      // e.g. endpoint: ...
    public <T> T deleteTaskStepExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId,
                                                @PathVariable Long stepReportId) {
        try {
            taskStepExecutionReportServiceImpl.deleteTaskStepExecutionReport(taskId, taskReportId, stepReportId);
            return (T) new ResponseEntity<String>("Deleted", HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchStepReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Get all the StepReports within a specific TaskReport,
     * sorted by either startDateTime or executionTimeSeconds.
     *
     * @param taskReportId
     * @param sortBy
     * @return
     * @param <T>
     */
    @Operation(summary = "Get all the StepReports within a specific TaskReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("""
                                            [
                                            	{
                                            		"id": 2,
                                            		"correspondingTaskReportId": 1,
                                            		"stepName": "step2",
                                            		"startDateTime": "2023-05-09T01:13:59.763364",
                                            		"endDateTime": "2023-05-09T01:13:59.763364",
                                            		"executionTimeSeconds": 0,
                                            		"status": "SUCCESS",
                                            		"errorMessage": "N/A"
                                            	},
                                            	{
                                            		"id": 1,
                                            		"correspondingTaskReportId": 1,
                                            		"stepName": "stepSuccessFinally",
                                            		"startDateTime": "2023-05-09T01:12:06.623236",
                                            		"endDateTime": "2023-05-09T01:13:45.657699",
                                            		"executionTimeSeconds": 99,
                                            		"status": "SUCCESS",
                                            		"errorMessage": "N/A new again "
                                            	}
                                            ]
                                            """)
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskStepExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskReportId passed.")
                            }
                    )
            })
    })
    @GetMapping("/taskReports/{taskReportId}/stepReports")
    public <T> T getAllTaskStepExecutionReportsForGivenTaskExecutionReport(@PathVariable Long taskReportId,
                                                                           @RequestParam Optional<String> sortBy) {
        if (sortBy.isPresent() && sortBy.get().equals("startDateTime")) {   // ...sort by startDateTime
            try {
                List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl.getAllTaskStepExecutionReportsSortedByStartDateTime(taskReportId);
                return (T) new ResponseEntity<List<TaskStepExecutionReport>>(taskStepExecutionReportList,
                        HttpStatus.OK);
            } catch (NoSuchTaskReportException e) {
                return (T) new ResponseEntity<String>(e.getMessage(),
                        HttpStatus.NOT_FOUND);
            }
        }
        if (sortBy.isPresent() && sortBy.get().equals("execTime")) {        // ...sort by executionTimeSeconds
            try {
                List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl.getAllTaskStepExecutionReportsSortedByExeTime(taskReportId);
                return (T) new ResponseEntity<List<TaskStepExecutionReport>>(taskStepExecutionReportList,
                        HttpStatus.OK);
            } catch (NoSuchTaskReportException e) {
                return (T) new ResponseEntity<String>(e.getMessage(),
                        HttpStatus.NOT_FOUND);
            }
        }
        try {                                                               // ... not sorted.
            List<TaskStepExecutionReport> taskStepExecutionReportList = taskStepExecutionReportServiceImpl.getAllTaskStepExecutionReportGivenTaskExecutionReport(taskReportId);
            return (T) new ResponseEntity<List<TaskStepExecutionReport>>(taskStepExecutionReportList,
                    HttpStatus.OK);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

}
