package com.example.ecommerce.inventory.model

import com.example.ecommerce.product.model.Product
import jakarta.persistence.*

@Entity
data class Stock(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    val product: Product? = null,
    var quantity: Int,
    var warehouse: String
)