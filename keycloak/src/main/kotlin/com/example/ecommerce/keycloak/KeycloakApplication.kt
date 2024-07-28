package com.example.ecommerce.keycloak

import com.example.ecommerce.keycloak.service.KeycloakService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    runApplication<KeycloakApplication>(*args)
}

@SpringBootApplication
class KeycloakApplication(private val keycloakService: KeycloakService) : CommandLineRunner {
    override fun run(vararg args: String) {
        keycloakService.createRealmAndConfigs()
        exitProcess(0)
    }
}