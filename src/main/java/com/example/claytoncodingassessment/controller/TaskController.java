package com.example.claytoncodingassessment.controller;

import com.example.claytoncodingassessment.model.entities.Task;
import com.example.claytoncodingassessment.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@Tag(name ="Task", description = "Get and Create Tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * Create a new Task.
     *
     * @param task
     * @return
     */
    @Operation(summary = "Create a new Task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success.", content = {
                    @Content(
                            mediaType = MediaType.TEXT_PLAIN_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = String.class)),
                            examples = {
                                    @ExampleObject("Task with generated id of 1 has been created.")
                            }
                    )
            })
    })
    @PostMapping("/tasks")
    public String createTask(@RequestBody Task task) {
        taskService.createTask(task);
        return "Task with generated id of " + task.getId() + " has been created.";
    }

    /**
     * Get all Tasks.
     *
     * @return
     */
    @Operation(summary = "Get all Tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "all the tasks", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = Task.class)),
                            examples = {
                                    @ExampleObject("""
                                            [
                                            	{
                                            		"id": 1,
                                            		"title": "Initial Task",
                                            		"taskExecutionReport": {
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
                                            	}
                                            ]
                                            """)
                            }
                    )
            })
    })
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

}
