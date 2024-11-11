package com.example.ecommerce.keycloak.dto.customer

data class GenericCustomerEventDto(
    val eventType: String,
    val customer: CustomerDTO
)

data class KeycloakCustomerEventDto(
    val eventType: String,
    val keycloakCustomer: KeycloakCustomerDTO
)

