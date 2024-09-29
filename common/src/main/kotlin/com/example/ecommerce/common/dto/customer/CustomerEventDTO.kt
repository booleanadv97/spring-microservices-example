package com.example.ecommerce.common.dto.customer

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class GenericCustomerEventDto(
    val eventType: String,
    val customer: CustomerDTO
)

@Embeddable
data class KeycloakCustomerEventDto(
    val eventType: String,
    @Embedded
    val keycloakCustomer: KeycloakCustomerDTO
)

