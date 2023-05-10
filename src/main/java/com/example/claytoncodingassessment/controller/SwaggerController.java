package com.example.claytoncodingassessment.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;


@RestController
@RequestMapping("/")
public class SwaggerController {

    @GetMapping
    @Operation(hidden = true)
    public void redirectWithUsingRedirectView(HttpServletResponse response) {
        response.setHeader("Location", "/swagger-ui/index.html");
        response.setStatus(302);
    }
}
