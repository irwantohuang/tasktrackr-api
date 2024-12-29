package com.veedo.tsk.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("TaskTrackr APP")
                        .version("v1.0.0")
                        .description("TaskTrackr API Documentation"));
    }

    @Bean
    GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("01. User")
                .pathsToMatch("/connection/**", "/tsk/users/**")
                .build();
    }

    @Bean
    GroupedOpenApi roleApi() {
        return GroupedOpenApi.builder()
                .group("02. Role")
                .pathsToMatch("/conncection/**", "/tsk/roles/**")
                .build();
    }

    @Bean
    GroupedOpenApi positionApi() {
        return GroupedOpenApi.builder()
                .group("03. Position")
                .pathsToMatch("/conncection/**", "/tsk/positions/**")
                .build();
    }

    @Bean
    GroupedOpenApi projectApi() {
        return GroupedOpenApi.builder()
                .group("04. Project")
                .pathsToMatch("/connection/**", "/tsk/projects/**")
                .build();
    }

    @Bean
    GroupedOpenApi taskApi() {
        return GroupedOpenApi.builder()
                .group("05. Tasks")
                .pathsToMatch("/connection/**", "/tsk/tasks/**")
                .build();
    }

    @Bean
    GroupedOpenApi statusAndPriorityApi() {
        return GroupedOpenApi.builder()
                .group("06. Status and Priority")
                .pathsToMatch("/connection/**", "/tsk/data/**")
                .build();
    }

    @Bean
    GroupedOpenApi summaryApi() {
        return GroupedOpenApi.builder()
                .group("07. Summary")
                .pathsToMatch("/connection/**", "/tsk/summary/**")
                .build();
    }

}
