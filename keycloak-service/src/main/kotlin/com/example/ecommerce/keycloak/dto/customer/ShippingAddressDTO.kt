package com.example.ecommerce.keycloak.dto.customer

data class ShippingAddressDTO(
    val shippingAddressId: Long,
    val recipientName: String,
    val address: AddressDTO,
)
