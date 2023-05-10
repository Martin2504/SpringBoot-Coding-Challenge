package com.example.claytoncodingassessment.service.serviceimpl;

import com.example.claytoncodingassessment.model.entities.Task;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class TaskServiceImplTest {

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @Test
    @DisplayName("Create A single task")
    void createSingleTaskTest() {
        Task taskToSave = Task.builder().title("Task1").build();
        Task taskActual = taskServiceImpl.createTask(taskToSave);
        assertThat(taskActual).usingRecursiveComparison().isEqualTo(taskToSave);
    }

    @Test
    @DisplayName("Test if title is not passed")
    public void TestMissingTitleCreateTask() {
        assertThrows(NullPointerException.class, () -> {
            Task.builder().build();;
        });
    }

    @Test
    @DisplayName("Testing all tasks are returned.")
    void getAllTasks() {
        taskServiceImpl.createTask(Task.builder().title("Task1").build());
        assertEquals(1, taskServiceImpl.getAllTasks().size() );
    }
}