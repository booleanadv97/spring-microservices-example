package com.example.ecommerce.order.dto

import com.example.ecommerce.order.dto.cart.CartItem
import com.example.ecommerce.order.dto.customer.ShippingAddressDTO
import com.example.ecommerce.order.model.OrderStatusEnum
import com.example.ecommerce.order.model.ShipmentStatusEnum
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

