package com.example.ecommerce.inventory.model

import jakarta.persistence.*

@Entity
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column( nullable = false)
    val productId: Long,
    @Column( nullable = false)
    var quantity: Int,
    @Column( nullable = false)
    var warehouse: String
)