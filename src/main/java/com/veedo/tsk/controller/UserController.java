package com.veedo.tsk.controller;

import com.veedo.tsk.request.UserCreateRequest;
import com.veedo.tsk.request.UserLoginRequest;
import com.veedo.tsk.request.UserUpdateRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    @Autowired
    private UserService userService;


    // GET ALL USER - START
    @Tags(value = @Tag(name = "1.1 Get All User"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"message\":\"Success\", " +
                                    "\"data\":[{\"user_id\":\"a977f438-57d1-4fc9-a3a1-fa9489857b8f\",\"role_name\":\"Admin\",\"position_name\":\"UI/UX\", " +
                                    "\"email\":\"john.doe@gmail.com\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"phone\":\"082280802121\"}, " +
                                    "{\"user_id\":\"a977f438-57d1-4fc9-a3a1-fa9489857b8f\",\"role_name\":\"Admin\",\"position_name\":\"UI/UX\", " +
                                    "\"email\":\"john.doe@gmail.com\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"phone\":\"082280802121\"}]}}")
                    )}
            )
    })
    @GetMapping("/tsk/users")
    public ResponseEntity<ResponseSchema> getAllUser(
            @Schema(hidden = true)
            @RequestHeader(name = "Authorization") String authorization) {
        return userService.getAllUser(authorization);
    }
    // GET ALL USER - END


    // GET SPECIFIC USER - START
    @Tags(value = @Tag(name = "1.2 Get User"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"message\":\"Success\", " +
                                    "\"data\":{\"user_id\":\"a977f438-57d1-4fc9-a3a1-fa9489857b8f\",\"role_name\":\"Admin\",\"position_name\":\"UI/UX\", " +
                                    "\"email\":\"john.doe@gmail.com\",\"first_name\":\"John\",\"last_name\":\"Doe\",\"phone\":\"082280802121\"}}}")
                    )}
            )
    })
    @GetMapping("/tsk/users/{email}")
    public ResponseEntity<ResponseSchema> getUser(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "email", required = true) String email) {
        return userService.getUser(authorization, email);
    }
    // GET SPECIFIC USER - END


    // CREATE USER - START
    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = "{\"email\":\"john.doe@gmail.com\",\"password\":\"query123\"," +
                                    "\"role_name\":\"Admin\",\"position_name\":\"UI/UX\",\"first_name\":\"John\"," +
                                    "\"last_name\":\"Doe\",\"phone\":\"082200010002\"}")
                    ),
                    description = "** Role & Position Are Not Required"
            )
    )
    @Tags(value = @Tag(name = "1.3 Create User"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            examples = @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}},\"output_schema\":{" +
                                    "\"status\":\"Success\",\"message\":{\"indonesian\":\"john.doe@gmail.com berhasil ditambah\"," +
                                    "\"english\":\"john.doe@gmail.com successfully added\"}}}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(name = "1. Email not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-001\",\"error_message\":" +
                                                    "{\"indonesian\":\"Email tidak valid\",\"english\":\"Email not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "2. Email already registered", value = "{\"error_schema\":{\"error_code\":\"TSK-01-002\",\"error_message\":" +
                                                    "{\"indonesian\":\"Email telah terdaftar\",\"english\":\"Email already registered\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "3. Email required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-003\",\"error_message\":" +
                                                    "{\"indonesian\":\"Email tidak boleh kosong\",\"english\":\"Email cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "4. Password required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-004\",\"error_message\":" +
                                                    "{\"indonesian\":\"Password tidak boleh kosong\",\"english\":\"Password cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "5. First name required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-005\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nama pertama tidak boleh kosong\",\"english\":\"First name cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "6. Phone number required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-006\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nomor telepon tidak boleh kosong\",\"english\":\"Phone number cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "7. Phone number not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-007\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nomor telepon tidak valid\",\"english\":\"Phone number not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "8. Role not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-008\",\"error_message\":" +
                                                    "{\"indonesian\":\"Role tidak valid\",\"english\":\"Role not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "9. Position not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-009\",\"error_message\":" +
                                                    "{\"indonesian\":\"Posisi kerja tidak valid\",\"english\":\"Position not valid\"}}, \"output_schema\":{}}"),


                                    }
                            )
                    }

            )
    })
    @PostMapping(
            path = "/tsk/users",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createUser(
            @RequestBody UserCreateRequest request) {
        return userService.createUser(request);
    }
    // CREATE USER - END

    // UPDATE USER - START
    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = "{\"password\":\"query123\"," +
                                    "\"role_name\":\"Admin\",\"position_name\":\"UI/UX\",\"first_name\":\"John\"," +
                                    "\"last_name\":\"Doe\",\"phone\":\"082200010002\"}")
                    ),
                    description = "** Role & Position Are Not Required"
            )
    )
    @Tags(value = @Tag(name = "1.4 Update User"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            examples = @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}},\"output_schema\":{" +
                                    "\"status\":\"Success\",\"message\":{\"indonesian\":\"john.doe@gmail.com berhasil di edit\"," +
                                    "\"english\":\"john.doe@gmail.com successfully updated\"}}}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(name = "1. Password required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-004\",\"error_message\":" +
                                                    "{\"indonesian\":\"Password tidak boleh kosong\",\"english\":\"Password cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "2. First name required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-005\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nama pertama tidak boleh kosong\",\"english\":\"First name cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "3. Phone number required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-006\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nomor telepon tidak boleh kosong\",\"english\":\"Phone number cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "4. Phone number not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-007\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nomor telepon tidak valid\",\"english\":\"Phone number not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "5. Role not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-008\",\"error_message\":" +
                                                    "{\"indonesian\":\"Role tidak valid\",\"english\":\"Role not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "6. Position not valid", value = "{\"error_schema\":{\"error_code\":\"TSK-01-009\",\"error_message\":" +
                                                    "{\"indonesian\":\"Posisi kerja tidak valid\",\"english\":\"Position not valid\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "7. Password cannot be same", value = "{\"error_schema\":{\"error_code\":\"TSK-01-012\",\"error_message\":" +
                                                    "{\"indonesian\":\"Password yang baru tidak boleh sama dengan yang lama\",\"english\":\"New password cannot be the same as your old password\"}}, \"output_schema\":{}}"),

                                    }
                            )
                    }

            )
    })
    @PostMapping(
            path = "/tsk/users/{email}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> updateUser(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "email", required = true) String email,
            @RequestBody UserUpdateRequest request) {
        System.out.println(" ===== DATA USER CONTROLLER =====");
        System.out.println("PATH VARIABLE EMAIL : " + email);
        System.out.println("PASSWORD " + request.getPassword());
        System.out.println("FIRST NAME " + request.getFirstName());
        System.out.println("LAST NAME " + request.getLastName());
        System.out.println("ROLE NAME " + request.getRoleName());
        System.out.println("POSITION NAME " + request.getPositionName());
        System.out.println("PHONE " + request.getPhone());
        System.out.println(" ");
        return userService.updateUser(authorization, request, email);
    }
    // UPDATE USER - END


    // UPLOAD PROFILE - START
//    @PostMapping(
//            path = "/tsk/users/{userId}/profile"
//    )
//    public ResponseEntity<ResponseSchema> uploadProfile(
//            @RequestHeader(name = "Authorization") String authorization,
//            @RequestParam("image") MultipartFile file,
//            @PathVariable("userId") String userId) throws IOException {
//        System.out.println("my files " +file);
//        return userService.uploadProfile(authorization, file, userId);
//    }
    // UPLOAD PROFILE - END

    // USER LOGIN - START
    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = "{\"email\":\"john.doe@gmail.com\",\"password\":\"query123\"}")
                    )
            )
    )
    @Tags(value = @Tag(name = "1.5 Login"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples = @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}},\"output_schema\":{" +
                                    "\"token\":\"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0MkBnbWFpbC5jb20iLCJleHAiOjE3MDM3NjA2MzksImlhdCI6MTcwMzc2MDU5NSwianRpIjoiZGI4ZTVlNjItOTg0Yi00ODhjLWE2MTMtYzJiYThlNDE1ZWRkIn0.D4J695wXFEyIDyDWolkivG5MB4SIHtDnM8kFhKntWKQvuCFVo0K9eunhGykUTXTos_MoQ0gex8r7KvJ2ZButwA\"" +
                                    ",\"expired_date\":\"43200\",\"email\":\"john.doe@gmail.com\",\"name\":\"John\"," +
                                    "\"role\":\"Admin\",\"position\":\"UI/UX\"}}")
                    )}),
            @ApiResponse(
                    responseCode = "400",
                    content = { @Content(
                            examples = {
                                    @ExampleObject(name = "1. Email not valid ", value = "{\"error_schema\":{\"error_code\":\"TSK-01-001\",\"error_message\":" +
                                            "{\"indonesian\":\"Email tidak valid\",\"english\":\"Email not valid\"}}, \"output_schema\":{}}"),
                                    @ExampleObject(name = "2. Email required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-003\",\"error_message\":" +
                                            "{\"indonesian\":\"Email tidak boleh kosong\",\"english\":\"Email cannot be empty\"}}, \"output_schema\":{}}"),
                                    @ExampleObject(name = "3. Password required", value = "{\"error_schema\":{\"error_code\":\"TSK-01-004\",\"error_message\":" +
                                            "{\"indonesian\":\"Password tidak boleh kosong\",\"english\":\"Password cannot be empty\"}}, \"output_schema\":{}}"),

                            }
                    )}),
            @ApiResponse(
                    responseCode = "401",
                    content = { @Content(
                            examples = {
                                    @ExampleObject(name = "1. Incorrect email", value = "{\"error_schema\":{\"error_code\":\"TSK-01-010\",\"error_message\":" +
                                            "{\"indonesian\":\"Email yang anda masukkan salah\",\"english\":\"The email you entered is incorrect\"}}, \"output_schema\":{}}"),
                                    @ExampleObject(name = "2. Incorrect password", value = "{\"error_schema\":{\"error_code\":\"TSK-01-011\",\"error_message\":" +
                                            "{\"indonesian\":\"Password yang anda masukkan salah\",\"english\":\"The password you entered is incorrect\"}}, \"output_schema\":{}}"),
                                    @ExampleObject(name = "3. User In Used", value = "{\"error_schema\":{\"error_code\":\"TSK-01-012\",\"error_message\":" +
                                            "{\"indonesian\":\"User sudah masuk ke aplikasi\",\"english\":\"User is already logged in to application\"}}, \"output_schema\":{}}"),
                            }
                    )})
    })
    @PostMapping(
            path = "/tsk/users/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> userLogin(
            @RequestBody UserLoginRequest request) {
        return userService.userLogin(request);
    }
    // USER LOGIN - END


    // GET CURRENT USER - START

    @Tags(value = @Tag(name = "1.6 Get Current User"))
    @GetMapping(
            path ="/tsk/users/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getCurrentUser(
            @RequestHeader(name = "Authorization") String authorization) {
        return userService.getCurrentUser(authorization);
    }

    // GET CURRENT USER - END


    // USER LOGOUT - START
    @Tags(value = @Tag(name = "1.7 Logout"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{}}")
                    )}
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = { @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-002\",\"error_message\":" +
                                    "{\"indonesian\":\"Token tidak valid\",\"english\":\"Invalid token\"}}, \"output_schema\":{}}")
                    )}
            )
    })
    @PostMapping(path = "/tsk/users/logout")
    public ResponseEntity<ResponseSchema> userLogout(
            @RequestHeader(name = "Authorization", required = true) String authorization) {
        return userService.userLogout(authorization);
    }
    // USER LOGOUT - END



}
