package com.example.ecommerce.product.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val productId: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    var price: Double,

    @Column(nullable = false)
    var categoryId: Long,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column( nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),
)