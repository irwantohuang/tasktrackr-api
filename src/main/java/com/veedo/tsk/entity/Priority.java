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
public class Priority {

    @JsonProperty("priority_id")
    private String priorityId;

    @JsonProperty("priority_name")
    private String priorityName;

}
