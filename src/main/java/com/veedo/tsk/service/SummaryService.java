package com.veedo.tsk.service;

import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.RoleRepository;
import com.veedo.tsk.repository.SummaryRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.response.SummaryResponse;
import com.veedo.tsk.schema.ResponseSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityConfig securityConfig;

    public ResponseEntity<ResponseSchema> getSummary(String authorization) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        SummaryResponse summaryResponse;

        if (user.getRoleName().equals(RoleRepository.MANAGER)) {
            summaryResponse = new SummaryResponse(summaryRepository.countProjects(), summaryRepository.countTasks(),
                    summaryRepository.countTaskInProgress(), summaryRepository.countTaskToDo());
        } else {
            summaryResponse = new SummaryResponse(summaryRepository.countProjects(), summaryRepository.countTasks(),
                    summaryRepository.countTaskInProgressByUser(user.getUserId()), summaryRepository.countTaskToDoByUser(user.getUserId()));
        }

        GeneralResponse response = new GeneralResponse(summaryResponse);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }
}
