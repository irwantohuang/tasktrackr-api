package com.veedo.tsk.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectRequest {


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


}
