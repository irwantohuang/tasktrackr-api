package com.veedo.tsk.controller;

import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.SummaryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class SummaryController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SummaryService summaryService;

    @Tags(value = @Tag(name = "7.1 Get Summary"))
    @GetMapping(
            path = "/tsk/summary",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getSummary(
            @RequestHeader(name = "Authorization") String authorization) {
        return summaryService.getSummary(authorization);
    }
}
