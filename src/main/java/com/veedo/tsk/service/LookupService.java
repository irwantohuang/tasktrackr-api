package com.veedo.tsk.service;

import com.veedo.tsk.entity.Position;
import com.veedo.tsk.entity.Role;
import com.veedo.tsk.entity.Status;
import com.veedo.tsk.repository.LookupRepository;
import com.veedo.tsk.repository.PositionRepository;
import com.veedo.tsk.repository.RoleRepository;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.response.LookupUserResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LookupService {

    @Autowired
    private LookupRepository lookupRepository;


    public ResponseEntity<ResponseSchema> getLookupUser() {
        List<LookupUserResponse> lookupUser = lookupRepository.findLookupUser();
        GeneralResponse response = new GeneralResponse(lookupUser);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> getLookupStatus() {
        List<Status> lookupStatus = lookupRepository.findLookupStatus();
        GeneralResponse response = new GeneralResponse(lookupStatus);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

}
