package com.example.ecommerce.keycloak.dto.customer

import java.time.LocalDateTime

data class CustomerDTO (
    val id: Long,
    val username: String,
    val email: String,
    val givenName: String,
    val familyName: String,
    var address: AddressDTO? = null,
    val shippingAddresses: MutableList<ShippingAddressDTO> = mutableListOf(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)