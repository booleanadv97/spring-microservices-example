package com.example.ecommerce.common.dto.cart

import com.example.ecommerce.common.dto.customer.ShippingAddressDTO
import com.example.ecommerce.common.dto.product.ProductDTO
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded

@Embeddable
data class CartEvent(
    val eventType: String,
    val userId: Long,
    @ElementCollection
    val cartItems: List<CartItem>,
    @Embedded
    val shippingAddress: ShippingAddressDTO? = null
)

@Embeddable
data class CartItem(
    val cartItemId: Long? = null,
    val product: ProductDTO,
    var quantity: Int
)