package com.veedo.tsk.service;

import com.veedo.tsk.config.ExceptionConfig;
import com.veedo.tsk.config.JwtTokenConfig;
import com.veedo.tsk.config.SecurityConfig;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.PositionException;
import com.veedo.tsk.exception.RoleException;
import com.veedo.tsk.exception.UserException;
import com.veedo.tsk.repository.PositionRepository;
import com.veedo.tsk.repository.RoleRepository;
import com.veedo.tsk.repository.UserRepository;
import com.veedo.tsk.request.UserCreateRequest;
import com.veedo.tsk.request.UserLoginRequest;
import com.veedo.tsk.request.UserUpdateRequest;
import com.veedo.tsk.response.LoginResponse;
import com.veedo.tsk.response.GeneralResponse;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.utils.Helpers;
import com.veedo.tsk.utils.UserValidation;
import io.micrometer.common.util.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private Logger log = LogManager.getLogger(UserService.class);
    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private UserValidation userValidation;

    @Autowired
    private Helpers helpers;

    public ResponseEntity<ResponseSchema> getAllUser(String authorization) {
        securityConfig.validateAuth(authorization);

        Object data = userRepository.findALl();
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> getUser(String authorization, String email) {
        securityConfig.validateAuth(authorization);
        Object data = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        System.out.println("[USER SERVICE] INI DATA USER " + data);
        GeneralResponse response = new GeneralResponse(data);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> createUser(UserCreateRequest request) {
        User checkUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        String roleName = (StringUtils.isBlank(request.getRoleName())) ? "Admin" : request.getRoleName();
        String positionName = (StringUtils.isBlank(request.getPositionName())) ? "None" : request.getPositionName();

        if (StringUtils.isBlank(request.getEmail()))
            throw UserException.EMAIL_REQUIRED;
        if (!userValidation.emailValidation(request.getEmail()))
            throw UserException.EMAIL_NOT_VALID;
        if (checkUser != null)
            throw UserException.EMAIL_ALREADY_REGISTERED;
        if (StringUtils.isBlank(request.getPassword()))
            throw UserException.PASSWORD_REQUIRED;
        if (StringUtils.isBlank(request.getFirstName()))
            throw UserException.FIRST_NAME_REQUIRED;
        if (StringUtils.isBlank(request.getPhone()))
            throw UserException.PHONE_REQUIRED;
        if (!userValidation.phoneValidation(request.getPhone()))
            throw UserException.PHONE_NOT_VALID;
        if (!roleRepository.isExistByRoleName(roleName))
            throw RoleException.ROLE_NOT_VALID;
        if (!positionRepository.isExistByPositionName(positionName))
            throw PositionException.POSITION_NOT_VALID;

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(securityConfig.encrypt(request.getPassword()));
        user.setRoleName(roleName);
        user.setPositionName(positionName);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());

        LocalDateTime time = LocalDateTime.now();
        String userId = userRepository.insertUser(user, time);

        String messageIn = request.getEmail() + " berhasil di tambah";
        String messageEn = request.getEmail() + " successfully added";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> updateUser(String authorization, UserUpdateRequest request, String email) {
        securityConfig.validateAuth(authorization);
        User checkUser = userRepository.findAccount(email).orElseThrow(() -> UserException.USER_NOT_FOUND);

        String roleName = (StringUtils.isBlank(request.getRoleName())) ? "Admin" : request.getRoleName();
        String positionName = (StringUtils.isBlank(request.getPositionName())) ? "None" : request.getPositionName();

        if (StringUtils.isBlank(request.getPassword()))
            throw UserException.PASSWORD_REQUIRED;
        if (securityConfig.checkPassword(request.getPassword(), checkUser.getPassword()))
            throw UserException.PASSWORD_CANNOT_BE_SAME;
        if (StringUtils.isBlank(request.getPhone()))
            throw UserException.PHONE_REQUIRED;
        if (!userValidation.phoneValidation(request.getPhone()))
            throw UserException.PHONE_NOT_VALID;
        if (!roleRepository.isExistByRoleName(roleName))
            throw RoleException.ROLE_NOT_VALID;
        if (!positionRepository.isExistByPositionName(positionName))
            throw PositionException.POSITION_NOT_VALID;
        if (StringUtils.isBlank(request.getFirstName()))
            throw UserException.FIRST_NAME_REQUIRED;

        User user = new User();
        user.setUserId(checkUser.getUserId());
        user.setPassword(securityConfig.encrypt(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRoleName(roleName);
        user.setPositionName(positionName);
        userRepository.updateUser(user);

        String messageIn = email + " berhasil di edit";
        String messageEn = email + " successfully updated";
        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
    }

    public ResponseEntity<ResponseSchema> userLogin(UserLoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (!userRepository.isUserInUsed(email)) {
            throw UserException.USER_ALREADY_LOGGED_IN;
        }
        if (StringUtils.isBlank(email))
            throw UserException.EMAIL_REQUIRED;
        if (!userValidation.emailValidation(request.getEmail()))
            throw UserException.EMAIL_NOT_VALID;
        if (StringUtils.isBlank(password))
            throw UserException.PASSWORD_REQUIRED;

        User checkUser = userRepository.findAccount(email)
                .orElseThrow(() -> UserException.INCORRECT_EMAIL);

        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.INCORRECT_EMAIL);

        if (securityConfig.checkPassword(password, checkUser.getPassword())) {
            UUID sessionId = userRepository.createLoginSession(email);
            String genToken = jwtTokenConfig.generateToken(email, sessionId.toString());

            log.info(email + " telah login ke Tasktrackr APP");
            GeneralResponse resp = new GeneralResponse(new LoginResponse(genToken, user));
            return new ResponseEntity<>(new ResponseSchema(
                    resp
            ), HttpStatus.OK);
        } else {
            throw UserException.INCORRECT_PASSWORD;
        }
    }

    public ResponseEntity<ResponseSchema> getCurrentUser(String authorization) {
        String email = securityConfig.validateAuth(authorization);
        User user = userRepository.findByEmail(email).orElse(null);

        GeneralResponse response = new GeneralResponse(user);
        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);

    }

    public ResponseEntity<ResponseSchema> userLogout(String authorization) {
        String email = securityConfig.validateAuth(authorization);
        userRepository.logoutSession(helpers.cleanToken(authorization));
        log.info(email + " telah logout dari Tasktrackr APP");

        throw ExceptionConfig.SUCCESS;
    }


//    public ResponseEntity<ResponseSchema> uploadProfile(String authorization, MultipartFile file, String userId) throws IOException {
//        String email = securityConfig.validateAuth(authorization);
//        User user = userRepository.findByEmail(email).orElseThrow(() -> UserException.USER_ALREADY_LOGGED_IN);
//
//        UUID id = userRepository.checkId(userId);
//        User checkUser = userRepository.findByUserId(id).orElseThrow(() -> UserException.EMAIL_REQUIRED);
//
//        userRepository.uploadProfile(file.getBytes(), id);
//        String messageIn = "Foto profil berhasil di tambah";
//        String messageEn = "Successfully added profile picture";
//        GeneralResponse response = new GeneralResponse(messageIn, messageEn);
//        return new ResponseEntity<>(new ResponseSchema(response), HttpStatus.OK);
//    }

}
