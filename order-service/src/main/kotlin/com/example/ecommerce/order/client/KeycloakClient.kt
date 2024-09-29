package com.example.ecommerce.order.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "keycloak-service")
interface KeycloakClient {
    @PostMapping("/keycloak/api/login_order")
    fun loginOrder(@RequestParam("username") username: String,
                      @RequestParam("password") password: String): String?
}