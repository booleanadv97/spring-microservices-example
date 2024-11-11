package com.example.ecommerce.inventory.dto.cart

import com.example.ecommerce.inventory.dto.customer.ShippingAddressDTO
import com.example.ecommerce.inventory.dto.product.ProductDTO

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