package com.veedo.tsk.service;

import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.Priority;
import com.veedo.tsk.repository.PriorityRepository;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PriorityService {

    @Autowired
    private PriorityRepository priorityRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public ResponseEntity<ResponseSchema> getAllPriority(String authorization) {
        securityConfig.validateAuth(authorization);

        List<Priority> priorityList = priorityRepository.findAllPriority();
        GeneralResponse response = new GeneralResponse(priorityList);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }
}
