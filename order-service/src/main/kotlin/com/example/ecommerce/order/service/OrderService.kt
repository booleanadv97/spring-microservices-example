package com.example.ecommerce.order.service

import com.example.ecommerce.order.dto.cart.CartEvent
import com.example.ecommerce.order.model.Order
import com.example.ecommerce.order.model.OrderStatusEnum
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface OrderService {
    fun create(cartEvent: CartEvent): Order?
    fun find(orderId: Long): Order?
    fun findAll(): List<Order>
    fun findOrders(customerId: Long): List<Order>?
    fun update(orderId: Long, status: OrderStatusEnum): Order?
    fun delete(orderId: Long)
    fun checkOrderAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean
}