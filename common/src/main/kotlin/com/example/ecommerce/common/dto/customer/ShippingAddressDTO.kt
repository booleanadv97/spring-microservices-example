package com.example.ecommerce.common.dto.customer

import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class ShippingAddressDTO(
    val shippingAddressId: Long,
    val recipientName: String,
    @Embedded
    val address: AddressDTO,
)
