package com.veedo.tsk.controller;

import com.veedo.tsk.request.PositionCreateRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.PositionService;
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
public class PositionController {

    @Autowired
    private PositionService positionService;

    @Tags(value = @Tag(name = "3.1 Get All Position"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"status\":\"Success\", " +
                                    "\"data\":[{\"position_id\":\"98775874-c7a2-4ec0-b6ef-f21b3acbbec7\",\"position_name\":\"UI/UX\"}," +
                                    "{\"position_id\":\"c05b9a66-ca69-4945-a4fe-4ad1bb03252d\",\"position_name\":\"Front End Developer\"}]}}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/positions",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getAllPosition(
            @RequestHeader(name = "Authorization") String authorization) {
        return positionService.getAllPosition(authorization);
    }


    @Tags(value = @Tag(name = "3.2 Get Position"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"status\":\"Success\", " +
                                    "\"data\":{\"position_id\":\"98775874-c7a2-4ec0-b6ef-f21b3acbbec7\",\"position_name\":\"UI/UX\"}}}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/positions/{positionId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getPosition(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "positionId") String positionId) {
        return positionService.getPosition(authorization, positionId);
    }


    @Tags(value = @Tag(name = "3.3 Create Position"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\"error_schema\":{\"error_code\":\"TSK-00-000\",\"error_message\":" +
                                    "{\"indonesian\":\"Berhasil\",\"english\":\"Success\"}}, \"output_schema\":{\"status\":\"Success\", " +
                                    "\"message\":{\"indonesian\":\"Front End Developer berhasil ditambah\"," +
                                    "\"english\":\"Front End Developer successfully added\"}}}")
                    )}
            )
    })
    @PostMapping(
            path = "/tsk/positions",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createPosition(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestBody PositionCreateRequest request) {
        return positionService.createPosition(authorization, request);
    }

}
