package com.example.ecommerce.product.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Product id", example = "1", nullable = true)
    val productId: Long = 0,

    @Column(nullable = false)
    @Schema(description = "Product name", example = "Smartphone S23", nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    @Schema(description = "Product description", example = "Experience cutting-edge technology with the Smartphone S23, designed to take mobile performance and photography to the next level. ", nullable = true)
    var description: String? = null,

    @Column(nullable = false)
    @Schema(description = "Product price", example = "100.00", nullable = false)
    var price: Double,

    @Column(nullable = false)
    @Schema(description = "Category id", example = "1", nullable = false)
    var categoryId: Long,

    @Column(nullable = false, updatable = false)
    @Schema(description = "Product creation time", example = " 2024-10-07T14:30:15.123", nullable = true)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(description = "Product last update", example = " 2024-10-07T14:30:15.123", nullable = true)
    @Column( nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)