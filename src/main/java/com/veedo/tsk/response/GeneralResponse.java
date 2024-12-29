package com.veedo.tsk.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.veedo.tsk.schema.CustomMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GeneralResponse {

    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private CustomMessage customMessage;

    @JsonProperty("data")
    private Object data;

    public GeneralResponse(String indonesian, String english) {
        this.status = "Success";
        this.customMessage = new CustomMessage(indonesian, english);
    }

    public GeneralResponse(Object data, String indonesian, String english) {
        this.status = "Success";
        this.data = data;
        this.customMessage = new CustomMessage(indonesian, english);
    }

    public GeneralResponse(Object data) {
        this.status = "Success";
        this.data = data;
    }



}
