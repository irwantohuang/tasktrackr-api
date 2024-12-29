package com.veedo.tsk.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserValidation {

    public Boolean emailValidation(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexPattern)
                .matcher(email)
                .matches();
    }

    public Boolean phoneValidation(String phone) {
        String pattern = "^(\\+62|62|0)8[1-9][0-9]{6,9}$";

        return Pattern.compile(pattern)
                .matcher(phone)
                .matches();
    }

}
