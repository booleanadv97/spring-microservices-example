package com.example.ecommerce.keycloak.config

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakConfig {
    @Value("\${keycloak.serverUrl}")
    private val keycloakServerUrl: String? = null

    @Value("\${keycloak.realm}")
    private val keycloakRealm: String? = null

    @Value("\${keycloak.resource}")
    private val keycloakClientId: String? = null

    @Value("\${keycloak.admin.username}")
    private val keycloakAdminUsername: String? = null

    @Value("\${keycloak.admin.password}")
    private val keycloakAdminPassword: String? = null

    @Bean
    fun buildClient() : Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm(keycloakRealm)
            .clientId(keycloakClientId)
            .username(keycloakAdminUsername)
            .password(keycloakAdminPassword)
            .build()
    }
}