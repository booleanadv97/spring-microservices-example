package com.example.ecommerce.cart.model

import jakarta.persistence.*

@Entity
@Table(name = "carts")
data class Cart(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val userId: Long,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER) val items: MutableList<CartItem> = mutableListOf()
)

@Entity
@Table(name = "cart_items")
data class CartItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    val productId: Long,
    var quantity: Int
)