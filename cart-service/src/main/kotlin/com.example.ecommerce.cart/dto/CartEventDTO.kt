package com.example.ecommerce.cart.dto

import com.example.ecommerce.cart.dto.customer.ShippingAddressDTO
import com.example.ecommerce.cart.dto.product.ProductDTO

data class CartEvent(
    val eventType: String,
    val customerId: Long,
    val cartItems: List<CartItem>,
    val shippingAddress: ShippingAddressDTO? = null
)

data class CartItem(
    val cartItemId: Long? = null,
    val product: ProductDTO,
    var quantity: Int
)