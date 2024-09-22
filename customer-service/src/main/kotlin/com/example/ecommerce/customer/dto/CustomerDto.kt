package com.example.ecommerce.customer.dto

import com.example.ecommerce.customer.model.Address


data class CustomerDto (
    val username: String,
    val email: String,
    val givenName: String,
    val familyName: String,
    var address: Address?,
)