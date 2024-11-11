package com.example.ecommerce.order.dto.product

import jakarta.persistence.Embeddable
import java.time.LocalDateTime


@Embeddable
data class ProductDTO(
    val productId: Long,
    var name: String,
    var description: String? = null,
    var price: Double,
    var categoryId: Long,
    val createdAt: LocalDateTime,
    var updatedAt: LocalDateTime,
)