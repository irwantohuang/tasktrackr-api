package com.veedo.tsk.controller;

import com.veedo.tsk.request.ProjectRequest;
import com.veedo.tsk.schema.ResponseSchema;
import com.veedo.tsk.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
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
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Tags(value = @Tag(name = "4.1.1 Get All Project"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\n" +
                                    "    \"error_schema\": {\n" +
                                    "        \"error_code\": \"TSK-00-000\",\n" +
                                    "        \"error_message\": {\n" +
                                    "            \"indonesian\": \"Berhasil\",\n" +
                                    "            \"english\": \"Success\"\n" +
                                    "        }\n" +
                                    "    },\n" +
                                    "    \"output_schema\": {\n" +
                                    "        \"status\": \"Success\",\n" +
                                    "        \"data\": [\n" +
                                    "            {\n" +
                                    "                \"project_id\": \"51596841-9f84-4952-99ce-4d0f5a8f3edc\",\n" +
                                    "                \"project_code\": \"PRO-TRC-004\",\n" +
                                    "                \"project_name\": \"New Customize Projects\",\n" +
                                    "                \"project_description\": \"\",\n" +
                                    "                \"start_date\": \"22/01/2025\",\n" +
                                    "                \"end_date\": \"27/01/2024\",\n" +
                                    "                \"project_manager\": \"John\",\n" +
                                    "                \"project_status\": \"In Progress\",\n" +
                                    "                \"team_members\": [\n" +
                                    "                    {\n" +
                                    "                        \"team_id\": \"94076172-8f60-4992-98ad-0895db92eada\",\n" +
                                    "                        \"team_member\": \"John\"\n" +
                                    "                    },\n" +
                                    "                    {\n" +
                                    "                        \"team_id\": \"d30e9d2e-1d81-463e-bf1a-eb172a91ff63\",\n" +
                                    "                        \"team_member\": \"Matthew\"\n" +
                                    "                    }\n" +
                                    "                ],\n" +
                                    "                \"created_date\": \"2024-01-09 02:50:47\",\n" +
                                    "                \"created_by\": \"Irwanto\"\n" +
                                    "            },\n" +
                                    "            {\n" +
                                    "                \"project_id\": \"791f1760-6a51-4987-96d9-c21fe801f8e4\",\n" +
                                    "                \"project_code\": \"PRO-TRC-002\",\n" +
                                    "                \"project_name\": \"Updated Projects\",\n" +
                                    "                \"project_description\": \"hello world\",\n" +
                                    "                \"start_date\": \"01/01/2022\",\n" +
                                    "                \"end_date\": \"01/01/2024\",\n" +
                                    "                \"project_manager\": \"John\",\n" +
                                    "                \"project_status\": \"Done\",\n" +
                                    "                \"team_members\": [],\n" +
                                    "                \"created_date\": \"2024-01-09 02:13:37\",\n" +
                                    "                \"created_by\": \"Irwanto\"\n" +
                                    "            }\n" +
                                    "        ]\n" +
                                    "    }\n" +
                                    "}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/projects",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getAllProjects(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestParam(name = "status", required = false) String status) {
        return projectService.getAllProjects(authorization, status);
    }


    @Tags(value = @Tag(name = "4.1.2 Get Project"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\n" +
                                    "    \"error_schema\": {\n" +
                                    "        \"error_code\": \"TSK-00-000\",\n" +
                                    "        \"error_message\": {\n" +
                                    "            \"indonesian\": \"Berhasil\",\n" +
                                    "            \"english\": \"Success\"\n" +
                                    "        }\n" +
                                    "    },\n" +
                                    "    \"output_schema\": {\n" +
                                    "        \"status\": \"Success\",\n" +
                                    "        \"data\": {\n" +
                                    "            \"project_id\": \"51596841-9f84-4952-99ce-4d0f5a8f3edc\",\n" +
                                    "            \"project_code\": \"PRO-TRC-004\",\n" +
                                    "            \"project_name\": \"New Customize Projects\",\n" +
                                    "            \"project_desc\": \"\",\n" +
                                    "            \"start_date\": \"22/01/2025\",\n" +
                                    "            \"end_date\": \"27/01/2024\",\n" +
                                    "            \"project_manager\": \"John\",\n" +
                                    "            \"project_status\": \"7da29ccb-3656-4ba7-b1c3-f827f0a87552\",\n" +
                                    "            \"team_members\": [\n" +
                                    "                \"John\",\n" +
                                    "                \"Matthew\"\n" +
                                    "            ],\n" +
                                    "            \"task_list\": [\n" +
                                    "                {\n" +
                                    "                    \"task_code\": \"TSK-TRC-009\",\n" +
                                    "                    \"task_name\": \"Contoh Project\",\n" +
                                    "                    \"asssigned_user\": \"John\",\n" +
                                    "                    \"start_date\": \"10/10/2024\",\n" +
                                    "                    \"end_date\": \"12/12/2024\",\n" +
                                    "                    \"task_status\": \"f2258097-1546-4131-9703-131702aa0534\",\n" +
                                    "                    \"task_priority\": \"1ee2ea0b-9910-4191-8161-4f4d60d78e08\"\n" +
                                    "                }\n" +
                                    "            ],\n" +
                                    "            \"created_date\": \"2024-01-09 02:50:47\",\n" +
                                    "            \"created_by\": \"Irwanto\"\n" +
                                    "        }\n" +
                                    "    }\n" +
                                    "}")
                    )}
            )
    })
    @GetMapping(
            path = "/tsk/projects/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> getProjectDetail(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId) {
        return projectService.getProjectDetail(authorization, projectId);
    }



    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"project_name\": \"UI/UX Landing Page\",\n" +
                                    "    \"project_desc\": \"\",\n" +
                                    "    \"start_date\": \"02/02/2024\",\n" +
                                    "    \"end_date\": \"03/03/2024\",\n" +
                                    "    \"project_manager\": \"irwantohng@gmail.com\",\n" +
                                    "    \"project_status\": \"To Do\"\n" +
                                    "}")
                    )
            )
    )
    @Tags(value = @Tag(name = "4.1.3 Create Project"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\n" +
                                    "    \"error_schema\": {\n" +
                                    "        \"error_code\": \"TSK-00-000\",\n" +
                                    "        \"error_message\": {\n" +
                                    "            \"indonesian\": \"Berhasil\",\n" +
                                    "            \"english\": \"Success\"\n" +
                                    "        }\n" +
                                    "    },\n" +
                                    "    \"output_schema\": {\n" +
                                    "        \"status\": \"Success\",\n" +
                                    "        \"message\": {\n" +
                                    "            \"indonesian\": \"Project 'UI/UX Landing Page' berhasil di tambah dengan nomor project: PRO-TRC-007\",\n" +
                                    "            \"english\": \"Project 'UI/UX Landing Page' successfully added with project number: PRO-TRC-007\"\n" +
                                    "        }\n" +
                                    "    }\n" +
                                    "}")
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(name = "1. Date required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-003\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal tidak boleh kosong\",\"english\":\"Date cannot be empty\"\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "1. Date required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-003\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal tidak boleh kosong\",\"english\":\"Date cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "2. Date format", value = "{\"error_schema\":{\"error_code\":\"TSK-06-004\",\"error_message\":" +
                                                    "{\"indonesian\":\"Format tanggal harus dd/MM/yyyy\",\"english\":\"Date format must be dd/MM/yyyy\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "3. Invalid date", value = "{\"error_schema\":{\"error_code\":\"TSK-06-005\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal berakhir tidak boleh lebih kecil dari tanggal mulai project.\",\"english\":\"End date cannot be less than project start date.\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "4. Project name required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-007\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nama project tidak boleh kosong\",\"english\":\"Project name cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "5. Project manager not found", value = "{\"error_schema\":{\"error_code\":\"TSK-06-006\",\"error_message\":" +
                                                    "{\"indonesian\":\"Project manager tidak ada\",\"english\":\"Project manager not found\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "6. Status not found", value = "{\"error_schema\":{\"error_code\":\"TSK-04-001\",\"error_message\":" +
                                                    "{\"indonesian\":\"Status tidak ada\",\"english\":\"Status not found\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "7. Status required", value = "{\"error_schema\":{\"error_code\":\"TSK-04-002\",\"error_message\":" +
                                                    "{\"indonesian\":\"Status tidak boleh kosong\",\"english\":\"Status cannot be empty\"}}, \"output_schema\":{}}"),
                                    }
                            )
                    }

            )
    })
    @PostMapping(
            path = "/tsk/projects",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> createProject(
            @RequestHeader(name = "Authorization") String authorization,
            @RequestBody ProjectRequest request) {
        return projectService.createProject(authorization, request);
    }




    @Operation(
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            examples = @ExampleObject(value = "{\n" +
                                    "    \"project_name\": \"Test Update Projects\",\n" +
                                    "    \"project_desc\": \"hello world lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum \",\n" +
                                    "    \"start_date\": \"01/01/2023\",\n" +
                                    "    \"end_date\": \"01/01/2024\",\n" +
                                    "    \"project_manager\": \"john.doe@gmail.com\",\n" +
                                    "    \"project_status\": \"Done\"\n" +
                                    "}")
                    )
            )
    )
    @Tags(value = @Tag(name = "4.1.4 Update Project"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\n" +
                                    "    \"error_schema\": {\n" +
                                    "        \"error_code\": \"TSK-00-000\",\n" +
                                    "        \"error_message\": {\n" +
                                    "            \"indonesian\": \"Berhasil\",\n" +
                                    "            \"english\": \"Success\"\n" +
                                    "        }\n" +
                                    "    },\n" +
                                    "    \"output_schema\": {\n" +
                                    "        \"status\": \"Success\",\n" +
                                    "        \"message\": {\n" +
                                    "            \"indonesian\": \"PRO-TRC-002 berhasil di update\",\n" +
                                    "            \"english\": \"PRO-TRC-002 successfully update\"\n" +
                                    "        }\n" +
                                    "    }\n" +
                                    "}")
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(name = "1. Date required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-003\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal tidak boleh kosong\",\"english\":\"Date cannot be empty\"\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "1. Date required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-003\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal tidak boleh kosong\",\"english\":\"Date cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "2. Date format", value = "{\"error_schema\":{\"error_code\":\"TSK-06-004\",\"error_message\":" +
                                                    "{\"indonesian\":\"Format tanggal harus dd/MM/yyyy\",\"english\":\"Date format must be dd/MM/yyyy\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "3. Invalid date", value = "{\"error_schema\":{\"error_code\":\"TSK-06-005\",\"error_message\":" +
                                                    "{\"indonesian\":\"Tanggal berakhir tidak boleh lebih kecil dari tanggal mulai project.\",\"english\":\"End date cannot be less than project start date.\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "4. Project name required", value = "{\"error_schema\":{\"error_code\":\"TSK-06-007\",\"error_message\":" +
                                                    "{\"indonesian\":\"Nama project tidak boleh kosong\",\"english\":\"Project name cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "5. Project manager not found", value = "{\"error_schema\":{\"error_code\":\"TSK-06-006\",\"error_message\":" +
                                                    "{\"indonesian\":\"Project manager tidak ada\",\"english\":\"Project manager not found\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "6. Status not found", value = "{\"error_schema\":{\"error_code\":\"TSK-04-001\",\"error_message\":" +
                                                    "{\"indonesian\":\"Status tidak ada\",\"english\":\"Status not found\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "7. Status required", value = "{\"error_schema\":{\"error_code\":\"TSK-04-002\",\"error_message\":" +
                                                    "{\"indonesian\":\"Status tidak boleh kosong\",\"english\":\"Status cannot be empty\"}}, \"output_schema\":{}}"),
                                            @ExampleObject(name = "8. Project not found", value = "{\"error_schema\":{\"error_code\":\"TSK-06-001\",\"error_message\":" +
                                                    "{\"indonesian\":\"Project tidak ada\",\"english\":\"Project not found\"}}, \"output_schema\":{}}"),
                                    }
                            )
                    }

            )
    })
    @PostMapping(
            path = "/tsk/projects/{projectId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> updateProject(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId,
            @RequestBody ProjectRequest request) {
        return projectService.updateProject(authorization, projectId, request);
    }





    @Tags(value = @Tag(name = "4.1.5 Delete Project"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = { @Content(
                            examples =  @ExampleObject(value = "{\n" +
                                    "    \"error_schema\": {\n" +
                                    "        \"error_code\": \"TSK-00-000\",\n" +
                                    "        \"error_message\": {\n" +
                                    "            \"indonesian\": \"Berhasil\",\n" +
                                    "            \"english\": \"Success\"\n" +
                                    "        }\n" +
                                    "    },\n" +
                                    "    \"output_schema\": {\n" +
                                    "        \"status\": \"Success\",\n" +
                                    "        \"message\": {\n" +
                                    "            \"indonesian\": \"PRO-TRC-001 berhasil di hapus\",\n" +
                                    "            \"english\": \"PRO-TRC-001 successfully delete\"\n" +
                                    "        }\n" +
                                    "    }\n" +
                                    "}")
                    )}
            ),
            @ApiResponse(
                    responseCode = "400",
                    content = {
                            @Content(
                                    examples = @ExampleObject(name = "1. Project not found", value = "{\"error_schema\":{\"error_code\":\"TSK-06-001\",\"error_message\":" +
                                                    "{\"indonesian\":\"Project tidak ada\",\"english\":\"Project not found\"}}, \"output_schema\":{}}")
                            )
                    }

            )
    })
    @DeleteMapping(
            path = "/tsk/projects/{projectId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ResponseSchema> deleteProject(
            @RequestHeader(name = "Authorization") String authorization,
            @PathVariable(name = "projectId") String projectId) {
        return projectService.deleteProject(authorization, projectId);
    }
}
