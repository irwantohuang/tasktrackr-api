package com.veedo.tsk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veedo.tsk.request.TeamsRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDetailResponse {

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("project_code")
    private String projectCode;

    @JsonProperty("project_name")
    private String projectName;

    @JsonProperty("project_desc")
    private String projectDesc;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("project_manager")
    private String projectManager;

    @JsonProperty("project_status")
    private String projectStatus;

    @JsonProperty("team_members")
    private List<String> teamMembers = new ArrayList<>();

    @JsonProperty("task_list")
    private List<ProjectTaskResponse> task = new ArrayList<>();

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("created_by")
    private String createdBy;

    public void addTeamMembers(String members) {
        teamMembers.add(members);
    }

    public void addTaskProject(ProjectTaskResponse taskProject) {
        task.add(taskProject);
    }

    public boolean hasTaskProject(String taskCode) {
            if (taskCode != null) {
                return task.stream().anyMatch(t -> t.getTaskCode().equals(taskCode));
            } else {
                return false;
            }
    }




}
