package com.example.ecommerce.keycloak.controller

import com.example.ecommerce.keycloak.dto.LoginRequest
import com.example.ecommerce.keycloak.service.KeycloakService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/keycloak/api/v1")
class KeycloakController(private val keycloakService: KeycloakService) {

    // Endpoint to login
    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponse(responseCode = "200", description = "Successful login")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    fun login(@RequestBody loginRequest: LoginRequest): Map<String, String> {
        return mapOf("jwt" to keycloakService.login(loginRequest.username, loginRequest.password))
    }
}