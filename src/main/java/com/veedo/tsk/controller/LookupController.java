package com.veedo.tsk.controller;

import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.LookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LookupController {

    @Autowired
    private LookupService lookupService;

    @GetMapping(
            path = "/tsk/lookup/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getLookupUser() {
        return lookupService.getLookupUser();
    }

    @GetMapping(
            path = "/tsk/lookup/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getLookupStatus() {
        return lookupService.getLookupStatus();
    }
}
