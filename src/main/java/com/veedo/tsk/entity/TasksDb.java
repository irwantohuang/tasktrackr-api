package com.veedo.tsk.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TasksDb {

    @JsonProperty("task_id")
    private String taskId;

    @JsonProperty("task_code")
    private String taskCode;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("task_description")
    private String taskDesc;

    @JsonProperty("task_assigned_to")
    private String taskAssignedTo;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("task_status")
    private String taskStatus;

    @JsonProperty("task_priority")
    private String taskPriority;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("last_update_date")
    private String lastUpdateDate;

    @JsonProperty("last_update_by")
    private String lastUpdateBy;

}
