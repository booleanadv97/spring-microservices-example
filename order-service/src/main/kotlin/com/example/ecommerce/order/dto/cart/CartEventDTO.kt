package com.example.ecommerce.order.dto.cart

import com.example.ecommerce.order.dto.customer.ShippingAddressDTO
import com.example.ecommerce.order.dto.product.ProductDTO
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

data class CartEvent(
    val eventType: String,
    val customerId: Long,
    val cartItems: List<CartItem>,
    val shippingAddress: ShippingAddressDTO? = null
)

@Embeddable
data class CartItem(
    val cartItemId: Long? = null,
    @Embedded
    val product: ProductDTO,
    var quantity: Int
)