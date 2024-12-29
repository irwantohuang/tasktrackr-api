package com.veedo.tsk.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomError extends RuntimeException {

    @JsonProperty("error_code")
    public String errorCode;

    @JsonProperty("indonesian")
    public String indonesian;

    @JsonProperty("english")
    public String english;

    @JsonProperty("http_status")
    public HttpStatus httpStatus;

}
