package com.example.ecommerce.customer.dto

data class GenericCustomerEventDto(
    val eventType: String,
    val customer: CustomerDTO
)

data class KeycloakCustomerEventDto(
    val eventType: String,
    val keycloakCustomer: KeycloakCustomerDTO
)

