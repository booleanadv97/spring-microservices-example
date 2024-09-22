package com.example.ecommerce.keycloak

import com.example.ecommerce.keycloak.service.KeycloakService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.context.event.EventListener

fun main(args: Array<String>) {
    runApplication<KeycloakApplication>(*args)
}

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class KeycloakApplication(private val keycloakService: KeycloakService) {

    @EventListener(ApplicationReadyEvent::class)
    fun onApplicationReady() {
        keycloakService.createRealmAndConfigs()
    }
}