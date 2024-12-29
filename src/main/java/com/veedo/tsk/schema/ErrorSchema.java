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
public class ErrorSchema {

    @JsonProperty("error_code")
    public String errorCode;

    @JsonProperty("error_message")
    public CustomMessage errorMessage;

    public ErrorSchema(String errorCode, String indonesian, String english) {
        this.errorCode = errorCode;
        this.errorMessage = new CustomMessage(indonesian, english);
    }

}
