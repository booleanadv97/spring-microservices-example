package com.example.ecommerce.inventory.dto.order

import com.example.ecommerce.inventory.dto.cart.CartItem
import com.example.ecommerce.inventory.dto.customer.ShippingAddressDTO
import java.time.LocalDateTime

data class OrderDTO(
    val id: Long?,
    val customerId: Long,
    val items: List<OrderItemDTO>,
    val shippingAddress: ShippingAddressDTO,
    var shippingStatus: ShipmentStatusEnum,
    val createdAt: LocalDateTime,
    var status: OrderStatusEnum
)

data class OrderItemDTO(
    val id: Long? = null,
    val cartItem: CartItem
)

enum class OrderStatusEnum {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    DECLINED,
    REFUNDED,
    CANCELLED
}

enum class ShipmentStatusEnum {
    DISPATCHED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    LOST,
}