package com.veedo.tsk.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomMessage {

    @JsonProperty("indonesian")
    private String indonesian;

    @JsonProperty("english")
    private String english;


}
