package com.example.ecommerce.inventory.exception

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now()
)