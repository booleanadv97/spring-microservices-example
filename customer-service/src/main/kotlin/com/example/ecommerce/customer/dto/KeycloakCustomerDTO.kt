package com.example.ecommerce.customer.dto

data class KeycloakCustomerDTO(
    val givenName: String?= null,
    val familyName: String?= null,
    val username: String,
    val updatedUsername: String?= null,
    val email: String,
    val password: String,
)