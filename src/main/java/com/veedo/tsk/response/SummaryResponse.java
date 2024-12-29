package com.veedo.tsk.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SummaryResponse {
    @JsonProperty("total_projects")
    private Long totalProjects;

    @JsonProperty("total_tasks")
    private Long totalTasks;

    @JsonProperty("task_in_progress")
    private Long taskInProgress;

    @JsonProperty("task_todo")
    private Long taksToDo;

}
