package com.example.ecommerce.customer.dto

data class ShippingAddressDTO(
    val shippingAddressId: Long,
    val recipientName: String,
    val address: AddressDTO,
)
