package com.veedo.tsk.service;

import com.veedo.tsk.config.ExceptionConfig;
import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.PositionException;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.PositionRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.request.PositionCreateRequest;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PositionService {

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;


    public ResponseEntity<ResponseSchema> getAllPosition(String authorization) {
        securityConfig.validateAuth(authorization);

        Object data = positionRepository.findAll();
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> getPosition(String authorization, String positionId) {
        securityConfig.validateAuth(authorization);
        Object data = positionRepository.findByPositionId(positionId).orElseThrow(() -> PositionException.POSITION_NOT_FOUND);
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> createPosition(String authorization, PositionCreateRequest request) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        if (!user.getRoleName().equals("Admin"))
            throw ExceptionConfig.NO_ACCESS;

        if (positionRepository.isExistByPositionName(request.getPositionName()))
            throw PositionException.POSITION_EXISTING;

        positionRepository.insertPosition(request.getPositionName());
        String messageIn = request.getPositionName() + " berhasil di tambah";
        String messageEn = request.getPositionName() + " successfully added";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);

    }
}
