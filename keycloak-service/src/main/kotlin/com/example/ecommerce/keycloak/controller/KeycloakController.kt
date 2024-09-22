package com.example.ecommerce.keycloak.controller

import com.example.ecommerce.keycloak.service.KeycloakService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/keycloak/api")
class KeycloakController(private val keycloakService: KeycloakService) {

    // Endpoint to customer login
    @PostMapping("/login_customer")
    @Operation(summary = "Customer login")
    @ApiResponse(responseCode = "200", description = "Successful customer login")
    fun loginCustomer(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = keycloakService.loginCustomer(username, password)
        return ResponseEntity.ok(jwt)
    }
}