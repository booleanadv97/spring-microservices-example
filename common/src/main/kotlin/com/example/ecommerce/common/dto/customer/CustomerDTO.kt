package com.example.ecommerce.common.dto.customer

import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import java.time.LocalDateTime

@Embeddable
data class CustomerDTO (
    val id: Long,
    val username: String,
    val email: String,
    val givenName: String,
    val familyName: String,
    @Embedded
    var address: AddressDTO? = null,
    @ElementCollection
    val shippingAddresses: List<ShippingAddressDTO> = listOf(),
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)