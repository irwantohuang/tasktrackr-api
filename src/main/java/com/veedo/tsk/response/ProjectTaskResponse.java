package com.veedo.tsk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectTaskResponse {

    @JsonProperty("task_code")
    private String taskCode;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("assigned_user")
    private String assignedUser;

    @JsonProperty("start_date")
    private String startDate;

    @JsonProperty("end_date")
    private String endDate;

    @JsonProperty("task_status")
    private String taskStatus;

    @JsonProperty("task_priority")
    private String taskPriority;



}
