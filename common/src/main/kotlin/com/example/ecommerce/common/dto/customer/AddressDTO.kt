package com.example.ecommerce.common.dto.customer

import jakarta.persistence.Embeddable

@Embeddable
data class AddressDTO(
    val streetAddress: String,
    val addressLocality: String,
    val addressRegion: String,
    val postalCode: String,
    val addressCountry: String
)