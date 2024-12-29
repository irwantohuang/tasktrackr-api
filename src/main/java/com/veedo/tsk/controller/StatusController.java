package com.veedo.tsk.controller;

import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.StatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @Tags(value = @Tag(name = "6.1 Get All Status"))
    @GetMapping(
            path = "/tsk/data/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getAllStatus(
            @RequestHeader(name = "Authorization") String authorization) {
        return statusService.getAllStatus(authorization);
    }

}
