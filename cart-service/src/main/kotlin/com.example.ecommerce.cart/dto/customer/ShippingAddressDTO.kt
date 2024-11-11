package com.example.ecommerce.cart.dto.customer

data class ShippingAddressDTO(
    val shippingAddressId: Long,
    val recipientName: String,
    val address: AddressDTO,
)
