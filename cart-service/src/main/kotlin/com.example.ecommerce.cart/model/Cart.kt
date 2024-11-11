package com.example.ecommerce.cart.model

import com.example.ecommerce.cart.dto.product.ProductDTO
import jakarta.persistence.*
import jakarta.persistence.GenerationType.IDENTITY

@Entity
@Table(name = "carts")
data class Cart(
    @Id @GeneratedValue(strategy = IDENTITY) val id: Long? = null,
    val customerId: Long,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true) val items: MutableList<CartItem> = mutableListOf()
)

@Entity
@Table(name = "cart_items")
data class CartItem(
    @Id @GeneratedValue(strategy = IDENTITY) val cartItemId: Long? = null,
    @Embedded
    val product: ProductDTO,
    var quantity: Int
)