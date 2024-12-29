package com.veedo.tsk.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.veedo.tsk.config.JwtTokenConfig;
import com.veedo.tsk.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {

    @JsonProperty("token")
    private String token;

    @JsonProperty("expired_date")
    private Long expiredDate;

    @JsonProperty("email")
    private String email;

    @JsonProperty("name")
    private String name;

    @JsonProperty("role")
    private String role;

    @JsonProperty("position")
    private String position;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.expiredDate = JwtTokenConfig.JWT_TOKEN_VALIDITY;
        this.email = user.getEmail();
        this.name = user.getFirstName();
        this.role = user.getRoleName();
        this.position = user.getPositionName();
    }

}
