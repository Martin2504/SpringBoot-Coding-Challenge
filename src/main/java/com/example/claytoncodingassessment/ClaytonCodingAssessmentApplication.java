package com.example.claytoncodingassessment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;


@SpringBootApplication
@OpenAPIDefinition(servers = @Server(url = "http://localhost:8080"))
public class ClaytonCodingAssessmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClaytonCodingAssessmentApplication.class, args);
    }

}
