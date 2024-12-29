package com.veedo.tsk.service;

import com.veedo.tsk.config.ExceptionConfig;
import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.*;
import com.veedo.tsk.exception.*;
import com.veedo.tsk.repository.*;
import com.veedo.tsk.request.TaskCreateRequest;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.utils.Helpers;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private Helpers helpers;


    public ResponseEntity<ResponseSchema> getAllTask(String authorization, String status, String priority) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        List<Tasks> tasksList = new ArrayList<>();
        if (user.getRoleName().equals(RoleRepository.MANAGER)) {
            tasksList = taskRepository.findAllTask(status, priority);
        } else {
            tasksList = taskRepository.findAllTaskByUser(user.getUserId(), status, priority);
        }

        GeneralResponse response = new GeneralResponse(tasksList);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> getTask(String authorization, String taskId) {
        String email = securityConfig.validateAuth(authorization);
        userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        UUID taskUuid = taskRepository.checkId(taskId);
        Tasks task = taskRepository.findTaskById(taskUuid, null).orElseThrow(() -> TaskException.TASK_NOT_FOUND);

        GeneralResponse response = new GeneralResponse(task);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> createTask(String authorization, TaskCreateRequest request, String projectId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(()-> UserException.USER_NOT_FOUND);
        UUID projId = projectRepository.checkId(projectId);
        Project checkProject = projectRepository.findProjectById(projId).orElseThrow(()-> ProjectException.PROJECT_NOT_FOUND);
        validateTaskRequest(request);
        if (teamRepository.isUserHasInTeam(request.getTaskAssignedTo(), projectId))
            throw TeamException.USER_NOT_IN_TEAM;

        if (!user.getUserId().equals(checkProject.getProjectManager()))
            throw ProjectException.NOT_PROJECT_MANAGER;

        int latestTaskCode = taskRepository.getTaskCode();
        String taskCode = helpers.genTaskCode(latestTaskCode);

        LocalDateTime time = LocalDateTime.now();
        taskRepository.insertTask(request, projectId, taskCode, user.getFirstName(), time);

        String messageIn = "Task '" + request.getTaskName() + "' berhasil di tambah, dengan task code: " + taskCode;
        String messageEn = "Task '" + request.getTaskName() + "' successfully added with task code: " + taskCode;
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public ResponseEntity<ResponseSchema> updateTask(String authorization, TaskCreateRequest request, String projectId, String taskId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(()-> UserException.USER_NOT_FOUND);
        UUID projUuid = projectRepository.checkId(projectId);
        Project checkProject = projectRepository.findProjectById(projUuid).orElseThrow(()-> ProjectException.PROJECT_NOT_FOUND);
        UUID taskUuid = taskRepository.checkId(taskId);
        Tasks myTask = taskRepository.findTaskById(taskUuid, projUuid).orElseThrow(() -> TaskException.TASK_NOT_FOUND);

        validateTaskRequest(request);
        if (teamRepository.isUserHasInTeam(request.getTaskAssignedTo(), projectId))
            throw TeamException.USER_NOT_IN_TEAM;
        if (!user.getUserId().equals(checkProject.getProjectManager()))
            throw ProjectException.NOT_PROJECT_MANAGER;

        LocalDateTime time = LocalDateTime.now();
        taskRepository.updateTask(request, taskUuid, projUuid, user.getFirstName(), time);

        String messageIn = myTask.getTaskCode() + " berhasil di update";
        String messageEn = myTask.getTaskCode() + " successfully update";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> deleteTask(String authorization, String taskId, String projectId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(()-> UserException.USER_NOT_FOUND);
        UUID projUuid = projectRepository.checkId(projectId);
        projectRepository.findProjectById(projUuid).orElseThrow(()-> ProjectException.PROJECT_NOT_FOUND);
        UUID taskUuid = taskRepository.checkId(taskId);
        Tasks myTask = taskRepository.findTaskById(taskUuid, projUuid).orElseThrow(() -> TaskException.TASK_NOT_FOUND);

        if (!user.getRoleName().equals(RoleRepository.MANAGER))
            throw ExceptionConfig.NO_ACCESS;
        LocalDateTime time = LocalDateTime.now();
        taskRepository.deleteTask(taskUuid, projUuid, time, user.getFirstName());
        String messageIn = myTask.getTaskCode() + " berhasil di hapus";
        String messageEn = myTask.getTaskCode() + " successfully delete";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public void validateTaskRequest(TaskCreateRequest request) {
        if (StringUtils.isBlank(request.getTaskName()))
            throw TaskException.TASK_NAME_REQUIRED;
        if (StringUtils.isBlank(request.getTaskAssignedTo()))
            throw TaskException.ASSIGNED_USER_REQUIRED;
        if (StringUtils.isBlank(request.getStartDate()) || StringUtils.isBlank(request.getEndDate()))
            throw TaskException.DATE_REQUIRED;
        if (helpers.convertToDate(request.getStartDate()).compareTo(helpers.convertToDate(request.getEndDate())) > 0)
            throw TaskException.INVALID_DATE;
        if (!helpers.isValidDate(request.getStartDate()) || !helpers.isValidDate(request.getEndDate()))
            throw TaskException.DATE_FORMAT;
        if (StringUtils.isBlank(request.getTaskStatus()))
            throw StatusException.STATUS_REQUIRED;
        if (StringUtils.isBlank(request.getTaskPriority()))
            throw PriorityException.PRIORITY_REQUIRED;

        User assignedUser = userRepository.findByEmail(request.getTaskAssignedTo()).orElseThrow(()-> UserException.USER_NOT_FOUND);
        Status status = statusRepository.findByStatusName(request.getTaskStatus()).orElseThrow(()-> StatusException.STATUS_NOT_FOUND);
        Priority priority = priorityRepository.findByPriorityName(request.getTaskPriority()).orElseThrow(()-> PriorityException.PRIORITY_NOT_FOUND);
        request.setTaskAssignedTo(assignedUser.getUserId());
        request.setTaskStatus(status.getStatusId());
        request.setTaskPriority(priority.getPriorityId());
    }

}
