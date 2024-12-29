package com.veedo.tsk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veedo.tsk.response.ProjectTeamResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project {

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("project_code")
    private String projectCode;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("project_description")
    private String projectDesc;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("project_manager")
    private String projectManager;

    @JsonProperty("manager_name")
    private String managerName;

    @JsonProperty("project_status")
    private String projectStatus;

    @JsonProperty("team_members")
    private List<ProjectTeamResponse> teamMembers = new ArrayList<>();

    @JsonProperty("total_task")
    private String totalTask;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("created_by")
    private String createdBy;

    public void addTeams(ProjectTeamResponse members) {
        teamMembers.add(members);
    }


    public boolean hasTeamInside(String teamId) {
        System.out.println("INI TEAM ID YOU " + teamId);
        if (teamId != null) {
            return teamMembers.stream().anyMatch(tm -> tm.getTeamId().equals(teamId));
        } else {
            return false;
        }
    }

}
