package com.example.ecommerce.common.dto.order

import com.example.ecommerce.common.dto.cart.CartItem
import com.example.ecommerce.common.dto.customer.ShippingAddressDTO
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import java.time.LocalDateTime

@Embeddable
data class OrderDTO(
    val id: Long?,
    val userId: Long,
    @ElementCollection
    val items: List<OrderItemDTO>,
    @Embedded
    val shippingAddress: ShippingAddressDTO,
    var shippingStatus: ShipmentStatusEnum,
    val createdAt: LocalDateTime,
    var status: OrderStatusEnum
)

@Embeddable
data class OrderItemDTO(
    val id: Long? = null,
    @Embedded
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