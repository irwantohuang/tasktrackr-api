package com.veedo.tsk.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @JsonProperty("position_id")
    private String positionId;

    @JsonProperty("position_name")
    private String positionName;

}
