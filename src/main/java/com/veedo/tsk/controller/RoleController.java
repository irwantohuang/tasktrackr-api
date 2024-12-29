package com.veedo.tsk.controller;

import com.veedo.tsk.request.RoleCreateRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.RoleService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Tags(value = @Tag(name = "2.1 Get All Role"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"message\":\"Success\", " +
                                    "\"data\":[{\"role_id\":\"6baac865-06f8-4f1d-a0b6-b6a434d13c8a\",\"role_name\":\"Admin\"}," +
                                    "{\"role_id\":\"42fbc1f2-4ea7-4eca-8d3b-bc60effa39e7\",\"role_name\":\"General\"}]}}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/roles",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getAllRole(
            @RequestHeader(name = "Authorization") String authorization) {
        return roleService.getAllRole(authorization);
    }

    @Tags(value = @Tag(name = "2.2 Get Role"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"status\":\"Success\", " +
                                    "\"data\":{\"role_id\":\"6baac865-06f8-4f1d-a0b6-b6a434d13c8a\",\"role_name\":\"Admin\"}}}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/roles/{roleId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getRole(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "roleId") String roleId) {
        return roleService.getRole(authorization, roleId);
    }


    @Tags(value = @Tag(name = "2.3 Create Role"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"status\":\"Success\", " +
                                    "\"message\":{\"indonesian\":\"General berhasil ditambah\",\"english\":\"General successfully added\"}}}")
                    )}
            )
    })
    @PostMapping(
            path = "/tsk/roles",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createRole(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestBody RoleCreateRequest request) {
        return roleService.createRole(authorization, request);
    }


    @Tags(value = @Tag(name = "2.4 Delete Role"))
    @DeleteMapping(
            path = "/tsk/roles/{roleId}"
    )
    public ResponseEntity<ResponseSchema> deleteRole(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "roleId") String roleId,
            @RequestBody RoleCreateRequest request) {
        return roleService.deleteRole(authorization, roleId, request);
    }

}
