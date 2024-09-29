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

    // Endpoint to inventory login
    @PostMapping("/login_inventory")
    @Operation(summary = "Inventory login")
    @ApiResponse(responseCode = "200", description = "Successful inventory login")
    fun loginInventory(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = keycloakService.loginInventory(username, password)
        return ResponseEntity.ok(jwt)
    }

    // Endpoint to product login
    @PostMapping("/login_product")
    @Operation(summary = "Product login")
    @ApiResponse(responseCode = "200", description = "Successful product login")
    fun loginProduct(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = keycloakService.loginProduct(username, password)
        return ResponseEntity.ok(jwt)
    }

    // Endpoint to order login
    @PostMapping("/login_order")
    @Operation(summary = "Order login")
    @ApiResponse(responseCode = "200", description = "Successful order login")
    fun loginOrder(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = keycloakService.loginOrder(username, password)
        return ResponseEntity.ok(jwt)
    }
}