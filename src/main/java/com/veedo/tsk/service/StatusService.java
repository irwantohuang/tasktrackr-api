package com.veedo.tsk.service;

import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.Status;
import com.veedo.tsk.repository.StatusRepository;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private SecurityConfig securityConfig;


    public ResponseEntity<ResponseSchema> getAllStatus(String authorization) {
        securityConfig.validateAuth(authorization);

        List<Status> status = statusRepository.findAllStatus();
        GeneralResponse response = new GeneralResponse(status);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }
}
