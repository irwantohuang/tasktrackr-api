package com.veedo.tsk.controller;

import com.veedo.tsk.request.TeamsRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TeamController {

    @Autowired
    private TeamService teamService;

    @Tags(value = @Tag(name = "4.3.1 Create Team Member"))
    @PostMapping(
            path = "/tsk/projects/{projectId}/teams",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createTeamMembers(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId,
            @RequestBody List<TeamsRequest> request) {
        return teamService.cdTeamMember(authorization, request, projectId);
    }


    @Tags(value = @Tag(name = "4.3.2 Delete Team Member"))
    @DeleteMapping(
            path = "/tsk/projects/{projectId}/teams/{teamId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> deleteTeamMember(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId,
            @PathVariable(name = "teamId") String teamId) {
        return teamService.deleteTeamMember(authorization, projectId, teamId);
    }
}
