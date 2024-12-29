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
public class ProjectTeamResponse {

    @JsonProperty("team_id")
    private String teamId;

    @JsonProperty("team_member")
    private String teamMember;

}
