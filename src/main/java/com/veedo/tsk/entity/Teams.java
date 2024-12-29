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
public class Teams {

    @JsonProperty("team_id")
    private String teamId;

    @JsonProperty("project_id")
    private String projectId;

    @JsonProperty("team_member_id")
    private String teamMemberId;

    @JsonProperty("team_member_status")
    private String teamMemberStatus;

    @JsonProperty("created_date")
    private String createdDate;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("last_update_date")
    private String lastUpdateDate;

    @JsonProperty("last_update_by")
    private String lastUpdateBy;

}
