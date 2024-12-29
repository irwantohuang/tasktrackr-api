package com.veedo.tsk.controller;

import com.veedo.tsk.request.TaskCreateRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.TaskService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Tags(value = @Tag(name = "5.1 Get All Tasks"))
    @GetMapping(
            path = "tsk/tasks",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getAllTask(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "priority", required = false) String priority) {
        return taskService.getAllTask(authorization, status, priority);
    }

    @Tags(value = @Tag(name = "5.2 Get Task"))
    @GetMapping(
            path = "/tsk/tasks/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getTask(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "taskId") String taskId) {
        return taskService.getTask(authorization, taskId);
    }


    @Tags(value = @Tag(name = "4.2.1 Create Task"))
    @PostMapping(
            path = "/tsk/projects/{projectId}/task",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createTask(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestBody TaskCreateRequest request,
            @PathVariable(name = "projectId") String projectId) {
        return taskService.createTask(authorization, request, projectId);
    }


    @Tags(value = @Tag(name = "4.2.2 Update Task"))
    @PostMapping(
            path = "/tsk/projects/{projectId}/task/{taskId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> updateTask(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestBody TaskCreateRequest request,
            @PathVariable(name = "projectId") String projectId,
            @PathVariable(name = "taskId") String taskId) {
        return taskService.updateTask(authorization, request, projectId, taskId);
    }

    @Tags(value = @Tag(name = "4.2.3 Delete Task"))
    @DeleteMapping(
            path = "/tsk/projects/{projectId}/task/{taskId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> deleteTask(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId,
            @PathVariable(name = "taskId") String taskId) {
        return taskService.deleteTask(authorization, taskId, projectId);
    }

}
