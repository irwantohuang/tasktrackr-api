package com.veedo.tsk.service;

import com.veedo.tsk.config.ExceptionConfig;
import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.Project;
import com.veedo.tsk.response.ProjectDetailResponse;
import com.veedo.tsk.entity.Status;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.ProjectException;
import com.veedo.tsk.exception.StatusException;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.ProjectRepository;
import com.veedo.tsk.repository.RoleRepository;
import com.veedo.tsk.repository.StatusRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.request.ProjectRequest;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.utils.Helpers;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private Helpers helpers;




    public ResponseEntity<ResponseSchema> getAllProjects(String authorization, String status) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        List<Project> projectList = new ArrayList<>();
        if (user.getRoleName().equals(RoleRepository.MANAGER)) {
            projectList = projectRepository.findAllProject(status);
        } else {
            projectList = projectRepository.findAllProjectByUser(user.getUserId(), status);
        }

        GeneralResponse response = new GeneralResponse(projectList);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public ResponseEntity<ResponseSchema> getProjectDetail(String authorization, String projectId) {
        securityConfig.validateAuth(authorization);
        UUID id = projectRepository.checkId(projectId);
//        ProjectDetail projectDetail = projectRepository.checkId(projectId);
        ProjectDetailResponse projectDetail = projectRepository.findProjectDetail(id);
        if (projectDetail == null) {
            throw ProjectException.PROJECT_NOT_FOUND;
        }
        GeneralResponse response = new GeneralResponse(projectDetail);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> createProject(String authorization, ProjectRequest request) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        int latestProjectCode = projectRepository.getProjectCode();
        String projectCode = helpers.genProjectCode(latestProjectCode);
        if (!user.getRoleName().equals(RoleRepository.MANAGER))
            throw ExceptionConfig.NO_ACCESS;

        validateProjectRequest(request);
        LocalDateTime time = LocalDateTime.now();

        projectRepository.insertProject(request, user.getFirstName(), projectCode, time);
        String messageIn = "Project '" + request.getProjectName() + "' berhasil di tambah dengan nomor project: " + projectCode;
        String messageEn = "Project '" + request.getProjectName() + "' successfully added with project number: " + projectCode;
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public ResponseEntity<ResponseSchema> updateProject(String authorization, String projectId, ProjectRequest request) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        UUID id = projectRepository.checkId(projectId);
        Project checkProject = projectRepository.findProjectById(id).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (!user.getRoleName().equals(RoleRepository.MANAGER) && !user.getEmail().equals(request.getProjectManager()))
            throw ExceptionConfig.NO_ACCESS;
        validateProjectRequest(request);

        LocalDateTime time = LocalDateTime.now();
        projectRepository.updateProject(request, id, time, user.getFirstName());
        String messageIn = checkProject.getProjectCode() + " berhasil di update";
        String messageEn = checkProject.getProjectCode() + " successfully update";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }



    public ResponseEntity<ResponseSchema> deleteProject(String authorization, String projectId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        UUID id = projectRepository.checkId(projectId);

        Project project = projectRepository.findProjectById(id).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
        if (!user.getRoleName().equals(RoleRepository.MANAGER))
            throw ExceptionConfig.NO_ACCESS;

        projectRepository.deleteProject(id);
        String messageIn = project.getProjectCode() + " berhasil di hapus";
        String messageEn = project.getProjectCode() + " successfully delete";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public void validateProjectRequest(ProjectRequest request) {
        if (StringUtils.isBlank(request.getStartDate()) || StringUtils.isBlank(request.getEndDate()))
            throw ProjectException.DATE_REQUIRED;
//        if (!helpers.isValidDate(request.getStartDate()) || !helpers.isValidDate(request.getEndDate()))
//            throw ProjectException.DATE_FORMAT;
        if (helpers.convertToDate(request.getStartDate()).compareTo(helpers.convertToDate(request.getEndDate())) > 0)
            throw ProjectException.INVALID_DATE;
        if (StringUtils.isBlank(request.getProjectName()))
            throw ProjectException.PROJECT_NAME_REQUIRED;
        if (StringUtils.isBlank(request.getProjectStatus()))
            throw StatusException.STATUS_REQUIRED;

        User managers = userRepository.findByEmail(request.getProjectManager()).orElseThrow(() -> ProjectException.PM_NOT_FOUND);
        request.setProjectManager(managers.getUserId());
        Status status = statusRepository.findByStatusName(request.getProjectStatus()).orElseThrow(() -> StatusException.STATUS_NOT_FOUND);
        request.setProjectStatus(status.getStatusId());
    }



}
