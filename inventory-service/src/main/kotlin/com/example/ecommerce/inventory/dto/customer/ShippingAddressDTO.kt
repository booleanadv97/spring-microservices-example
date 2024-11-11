package com.example.ecommerce.inventory.dto.customer

data class ShippingAddressDTO(
    val shippingAddressId: Long,
    val recipientName: String,
    val address: AddressDTO,
)
