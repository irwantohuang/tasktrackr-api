package com.veedo.tsk.service;

import com.veedo.tsk.config.ExceptionConfig;
import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.RoleException;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.RoleRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.request.RoleCreateRequest;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;


    public ResponseEntity<ResponseSchema> getAllRole(String authorization) {
        securityConfig.validateAuth(authorization);

        Object data = roleRepository.findAll();
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> getRole(String authorization, String roleId) {
        securityConfig.validateAuth(authorization);

        Object data = roleRepository.findByRoleId(roleId).orElseThrow(() -> RoleException.ROLE_NOT_FOUND);
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> createRole(String authorization, RoleCreateRequest request) {
        String email = securityConfig.validateAuth(authorization);

        if (roleRepository.isExistByRoleName(request.getRoleName()))
            throw RoleException.ROLE_EXISTING;

        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);
        if (!user.getRoleName().equals("Admin"))
            throw ExceptionConfig.NO_ACCESS;

        roleRepository.insertRole(request.getRoleName());
        String messageIn = request.getRoleName() + " berhasil di tambah";
        String messageEn = request.getRoleName() + " successfully added";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }


    public ResponseEntity<ResponseSchema> deleteRole(String authorization, String roleId, RoleCreateRequest request) {
        securityConfig.validateAuth(authorization);
        System.out.println("[ROLE REPO] ROLE ID : " + roleId);

        roleRepository.findByRoleId(roleId).orElseThrow(() -> RoleException.ROLE_NOT_FOUND);
        roleRepository.deleteRole(roleId);
        String messageIn = "role berhasil di hapus";
        String messageEn = "role successfully deleted";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);



    }


}
