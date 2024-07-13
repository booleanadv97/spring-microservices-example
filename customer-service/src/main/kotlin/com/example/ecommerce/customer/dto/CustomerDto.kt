package com.example.ecommerce.customer.dto

import com.example.ecommerce.customer.model.Address


data class CustomerDto (
    val id: Long,
    val email: String,
    val givenName: String,
    val familyName: String,
    var password: String,
    var address: Address,
)