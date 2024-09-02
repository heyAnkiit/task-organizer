package com.task_organizer.configuration;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
/**
 * <p>
 *  Swagger Configuration class equipped with JWT-based authentication.
 *
 * @author  Ankit
 */

@Configuration
@OpenAPIDefinition
@ComponentScan(basePackageClasses = SpringDocConfiguration.class)
public class SpringDocConfiguration {

    @Bean
    public OpenAPI baseOpenAPI() {
        return new OpenAPI()
                .servers(
                        getServerList())
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()))
                .info(new Info().title("Task Organizer Service")
                        .description("API reference guide for Task Organizer developers")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Contact- Ankit")
                        .url("https://Contact_Ankit"))

                ;
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * <p>
     *     In this method, backend server 'URLs' are incorporated, granting them authorization for utilization by Swagger in documentation operations.
     */
    private List<Server> getServerList(){
        Server localhost = new Server();
        localhost.setUrl("http://localhost:8080");
        localhost.setDescription("Localhost");

        return Arrays.asList(localhost);
    }
}
