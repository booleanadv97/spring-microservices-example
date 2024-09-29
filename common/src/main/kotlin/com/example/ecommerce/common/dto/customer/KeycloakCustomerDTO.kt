package com.example.ecommerce.common.dto.customer

import jakarta.persistence.Embeddable

@Embeddable
data class KeycloakCustomerDTO(
    val givenName: String?= null,
    val familyName: String?= null,
    val username: String,
    val email: String,
    val password: String,
)