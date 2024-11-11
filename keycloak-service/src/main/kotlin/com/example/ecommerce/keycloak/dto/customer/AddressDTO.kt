package com.example.ecommerce.keycloak.dto.customer

data class AddressDTO(
    val streetAddress: String,
    val addressLocality: String,
    val addressRegion: String,
    val postalCode: String,
    val addressCountry: String
)