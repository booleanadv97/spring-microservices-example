package com.example.ecommerce.customer.dto

data class AddressDTO(
    val streetAddress: String,
    val addressLocality: String,
    val addressRegion: String,
    val postalCode: String,
    val addressCountry: String
)