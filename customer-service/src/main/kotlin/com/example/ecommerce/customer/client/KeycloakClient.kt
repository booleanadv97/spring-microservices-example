package com.example.ecommerce.customer.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "keycloak-service")
interface KeycloakClient {
    @PostMapping("/keycloak/api/login_customer")
    fun loginCustomer(@RequestParam("username") username: String,
                      @RequestParam("password") password: String): String?
}