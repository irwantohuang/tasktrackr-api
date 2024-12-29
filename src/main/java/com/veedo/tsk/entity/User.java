package com.veedo.tsk.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("role_name")
    private String roleName;

    @JsonProperty("position_name")
    private String positionName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

//    @Lob
//    @JsonProperty("profile_image")
//    private byte[] profileImage;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("phone")
    private String phone;

}
