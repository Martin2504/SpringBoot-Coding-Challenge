package com.example.claytoncodingassessment.controller;

import com.example.claytoncodingassessment.model.Status;
import com.example.claytoncodingassessment.model.entities.TaskExecutionReport;
import com.example.claytoncodingassessment.service.TaskExecutionReportService;
import com.example.claytoncodingassessment.service.exceptions.AlreadySuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskException;
import com.example.claytoncodingassessment.service.exceptions.NoSuchTaskReportException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@Tag(name ="TaskExecutionReport", description = "CRUD operations for TaskExecutionReport resource.")
public class TaskExecutionReportController {

    @Autowired
    private TaskExecutionReportService taskExecutionReportService;

    /**
     * Create a TaskReport.
     *
     * @param taskId
     * @param taskExecutionReport
     * @return
     * @param <T>
     */
    @Operation(summary = "Create a new TaskReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("TaskReport with id 1 has been added.")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("A TaskReport for task with id 1 already exists.")
                            }
                    )
            })
    })
    @PostMapping("/task/{taskId}/taskReports")
    public <T> T setTaskExecutionReport(@PathVariable Long taskId, @RequestBody TaskExecutionReport taskExecutionReport) {      // Here @RequestBody annotation means in the body of the
                                                                                                                                // HTTP request, there will be an TaskExecutionReport object.
        try{
            taskExecutionReportService.createTaskExecutionReport(taskId, taskExecutionReport);
            log.info("TaskReport with id " + taskExecutionReport.getId() + " has been added.");
            return (T) new ResponseEntity<String>("TaskReport with id " + taskExecutionReport.getId() + " has been added",
                    HttpStatus.CREATED);
        } catch (NoSuchTaskException e) {
            log.error(e.getMessage(), e.getStackTrace());
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (AlreadySuchTaskException e) {
            log.error(e.getMessage(), e.getStackTrace());
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get all TaskReports,
     * or Get all TaskReports with a specific status,
     * or Get all TaskReports sorted by execution time.
     *
     * @param status
     * @param sortBy
     * @return
     */
    @Operation(summary = "Get all TaskReports.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("""
                                            [
                                            	{
                                            		"id": 1,
                                            		"startDateTime": "2023-05-09T23:55:30.961626",
                                            		"endDateTime": "2023-05-09T23:55:33.151368",
                                            		"executionTimeSeconds": 0,
                                            		"errorMessage": "N/A",
                                            		"status": "SUCCESS",
                                            		"taskStepExecutionReports": [
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
                                            	}
                                            ]
                                            """)
                            }
                    )
            }),
    })
    @GetMapping("/tasks/taskReports")
    public List<TaskExecutionReport> getAllTaskExecutionReports(@RequestParam Optional<String> status, @RequestParam Optional<String> sortBy) {
        if (status.isPresent() && Status.contains(status.get())) {
            return taskExecutionReportService.getAllTaskExecutionReportsByStatus(Status.valueOf(status.get()));
        }
        if (sortBy.isPresent() && sortBy.get().equals("execTime")) {
            return taskExecutionReportService.getAllTaskExecutionReportsOrderedByExeTime();
        }
        return taskExecutionReportService.getAllTaskExecutionReports();
    }

    /**
     * Get a specific TaskReport.
     *
     * @param taskId
     * @param taskReportId
     * @return
     * @param <T>
     */
    @Operation(summary = "Get a specific TaskReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("""
                                            {
                                            	"id": 1,
                                            	"startDateTime": "2023-05-09T23:33:24.982218",
                                            	"endDateTime": null,
                                            	"executionTimeSeconds": null,
                                            	"errorMessage": "N/A",
                                            	"status": null,
                                            	"taskStepExecutionReports": []
                                            }
                                            """)
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @GetMapping("/tasks/{taskId}/taskReports/{taskReportId}")
    public <T> T getTaskExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId) {
        try {
            TaskExecutionReport TaskExecutionReport = taskExecutionReportService.getTaskExecutionReport(taskId, taskReportId);
            return (T) new ResponseEntity<>(TaskExecutionReport, HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Update a specific TaskReport.
     *
     * @param taskId
     * @param taskReportId
     * @param taskExecutionReport
     * @return
     * @param <T>
     */
    @Operation(summary = "Update a specific TaskReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Updated")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @PutMapping("/tasks/{taskId}/taskReports/{taskReportId}")
    public <T> T updateTaskExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId,
                                           @RequestBody TaskExecutionReport taskExecutionReport) {
        try {
            taskExecutionReportService.updateTaskExecutionReport(taskId, taskReportId, taskExecutionReport);
            return (T) new ResponseEntity<String>("Updated", HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Delete a specific TaskReport.
     *
     * @param taskId
     * @param taskReportId
     * @return
     * @param <T>
     */
    @Operation(summary = "Delete a specific TaskReport.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Deleted")
                            }
                    )
            }),
            @ApiResponse(responseCode = "404", description = "Not Found.", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskExecutionReport.class)),
                            examples = {
                                    @ExampleObject("Non-existent taskId passed.")
                            }
                    )
            })
    })
    @DeleteMapping("/tasks/{taskId}/taskReports/{taskReportId}")
    public <T> T deleteTaskExecutionReport(@PathVariable Long taskId, @PathVariable Long taskReportId) {

        try {
            taskExecutionReportService.deleteTaskExecutionReport(taskId, taskReportId);
            return (T) new ResponseEntity<String>("Deleted", HttpStatus.OK);
        } catch (NoSuchTaskException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        } catch (NoSuchTaskReportException e) {
            return (T) new ResponseEntity<String>(e.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

}
