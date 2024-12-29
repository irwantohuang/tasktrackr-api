package com.veedo.tsk.service;

import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.Project;
import com.veedo.tsk.entity.Teams;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.ProjectException;
import com.veedo.tsk.exception.TeamException;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.ProjectRepository;
import com.veedo.tsk.repository.TeamRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.request.TeamsRequest;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.utils.UserValidation;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TeamService {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserValidation userValidation;

//    public ResponseEntity<ResponseSchema> checkTeamMember(String authorization, List<TeamsRequest> requests,
//                                                          String projectId) {
//        String email = securityConfig.validateAuth(authorization);
//        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
//
//        UUID projectUUID = projectRepository.checkId(projectId);
//        Project project = projectRepository.findProjectById(projectUUID).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
//
//        if (!user.getUserId().equals(project.getProjectId()))
//            throw ProjectException.NOT_PROJECT_MANAGER;
//
//        LocalDateTime createdDate = LocalDateTime.now();
//
//        Set<String> uniqueEmails = new HashSet<>();
//        for (TeamsRequest request : requests) {
//            if (StringUtils.isBlank(request.getEmail()))
//                throw UserException.EMAIL_REQUIRED;
//            if (!userValidation.emailValidation(request.getEmail()))
//                throw  UserException.EMAIL_NOT_VALID;
//
//            User teamUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
//
//            if (uniqueEmails.contains(request.getEmail()))
//                throw TeamException.DUPLICATE;
//            uniqueEmails.add(request.getEmail());
//        }
//
//
//        for (TeamsRequest request : requests) {
//            User teamUser = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
//            Boolean isUserHasInTeam = teamRepository.isUserHasInTeam(teamUser.getUserId(), projectId);
//
//            if (isUserHasInTeam) {
//
//            }
//        }
//
//
//    }


    public ResponseEntity<ResponseSchema> cdTeamMember(String authorization, List<TeamsRequest> requests,
                                                       String projectId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        UUID projectUUID = projectRepository.checkId(projectId);
        Project project = projectRepository.findProjectById(projectUUID).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);

        if (!user.getUserId().equals(project.getProjectManager()))
            throw ProjectException.NOT_PROJECT_MANAGER;

        LocalDateTime createdDate = LocalDateTime.now();

        checkTeamMember(requests, projectUUID);

        return new ResponseEntity<>(new ResponseSchema(" "), HttpStatus.OK);
    }

    public void checkTeamMember(List<TeamsRequest> requests, UUID projectUUID) {

        Set<String> emailSet = new HashSet<>();
        List<String> uniqueEmails = new ArrayList<>();

        List<Teams> currentTeams = teamRepository.findAllMemberByProjectId(projectUUID);

        for (TeamsRequest request : requests) {
            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
            emailSet.add(user.getUserId());

        }
        System.out.println("ini email set" + emailSet);

        for (Teams current : currentTeams) {
            if (!emailSet.contains(current.getTeamMemberId())) {
                System.out.println("ada email tak  sama : " + current.getTeamMemberId());
                uniqueEmails.add(current.getTeamMemberId());
            } else {
                System.out.println("email yang sama " + current.getTeamMemberId());
            }
        }

        if (uniqueEmails.isEmpty()) {
            System.out.println("tidak ada yang email yang tidak sama");
        } else {
            for (String email : uniqueEmails) {
                System.out.println("ini email yang tak sama :" + email);
            }
        }



//        for (TeamsRequest request : requests) {
//            User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
//            for (Teams current : currentTeams) {
//                if (current.getTeamMemberId().equals(user.getUserId())) {
//                    System.out.println("check equals : " + user.getUserId());
//                    System.out.println("check equals repo: " + current.getTeamMemberId());
//                    System.out.println("ini user yang ada di projects : " + user.getEmail());
//                    break;
//                } else {
//                    System.out.println("check equals : " + user.getUserId());
//                    System.out.println("check equals repo: " + current.getTeamMemberId());
//                    System.out.println("ini user yang tak ada di projects : " + user.getEmail());
//                    break;
//                }
//            }
//        }
    }


    public ResponseEntity<ResponseSchema> createTeamMember(String authorization, List<TeamsRequest> request, String projectId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        UUID projId = projectRepository.checkId(projectId);
        Project project = projectRepository.findProjectById(projId).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
        System.out.println("CEK DATA INI : " + user.getUserId() + " === " + project.getProjectManager());
        if (!user.getUserId().equals(project.getProjectManager()))
            throw ProjectException.NOT_PROJECT_MANAGER;
        LocalDateTime time = LocalDateTime.now();

        Set<String> uniqueEmails = new HashSet<>();
        for (TeamsRequest req : request) {
            if (StringUtils.isBlank(req.getEmail()))
                throw UserException.EMAIL_REQUIRED;
            if (!userValidation.emailValidation(req.getEmail()))
                throw UserException.EMAIL_NOT_VALID;
            User teamUser = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
            if (uniqueEmails.contains(req.getEmail())) {
                throw TeamException.DUPLICATE;
            }
            uniqueEmails.add(req.getEmail());

//            if (!teamRepository.isUserHasInTeam(teamUser.getUserId(), projectId))
//                throw TeamException.MEMBER_HAS_IN_TEAM;
        }

        for (TeamsRequest req : request) {
            User teamUser = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> UserException.USER_NOT_FOUND);
            teamRepository.insertTeams(projId, teamUser.getUserId(), user.getFirstName(), time);
        }

        GeneralResponse response = new GeneralResponse("yes", "ya");
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }



    public ResponseEntity<ResponseSchema> deleteTeamMember(String authorization, String projectId, String teamId) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        UUID projId = projectRepository.checkId(projectId);
        Project project = projectRepository.findProjectById(projId).orElseThrow(() -> ProjectException.PROJECT_NOT_FOUND);
        if (user.getUserId().equals(project.getProjectManager()))
            throw ProjectException.NOT_PROJECT_MANAGER;
        LocalDateTime time = LocalDateTime.now();

        UUID teamUuid = null;
        try {
            teamUuid = UUID.fromString(teamId);
        } catch (Exception e) {
            throw TeamException.MEMBER_HAS_IN_TEAM;
        }
        Teams teams = teamRepository.findTeamMemberById(teamUuid).orElseThrow(() -> TeamException.MEMBER_NOT_FOUND);
        User teamUser = userRepository.findByUserId(UUID.fromString(teams.getTeamMemberId())).orElseThrow(() -> UserException.USER_NOT_FOUND);
        Project teamProject = projectRepository.findProjectById(UUID.fromString(teams.getProjectId())).orElseThrow(()-> ProjectException.PROJECT_NOT_FOUND);
        teamRepository.deleteTeamMember(teams.getTeamMemberId(), time, user.getFirstName());

        String messageIn = teamUser.getFirstName() + " berhasil di hapus dari project " + teamProject.getProjectName();
        String messageEn = "Successfully delete " + teamUser.getFirstName() + " from project " + teamProject.getProjectName();
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);

    }
}
