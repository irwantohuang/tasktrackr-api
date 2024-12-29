package com.veedo.tsk.config;

import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.UUID;

@Configuration
public class SecurityConfig {

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private Helpers helpers;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private UserRepository userRepository;

    public SecurityConfig() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    public String encrypt(String password) {
        return passwordEncoder.encode(password);
    }

    public Boolean checkPassword(String password, String encodePassword) {
        return passwordEncoder.matches(password, encodePassword);
    }

    public String validateAuth(String authorization) {
        if (authorization == null)
            throw ExceptionConfig.AUTHORIZATION;

        String bearerToken = helpers.cleanToken(authorization);
        String email = jwtTokenConfig.getEmailFromToken(bearerToken);

        if (!jwtTokenConfig.validateToken(bearerToken, email)) {
            throw ExceptionConfig.INVALID_TOKEN;
        }

        UUID sessionId = userRepository.getActiveSession(bearerToken);
        if (sessionId == null) {
            throw ExceptionConfig.INVALID_TOKEN;
        }

        return email;
    }

}
