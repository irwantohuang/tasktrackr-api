package com.veedo.tsk.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskCreateRequest {
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
}
