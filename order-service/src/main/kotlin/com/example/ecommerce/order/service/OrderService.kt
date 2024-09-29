package com.example.ecommerce.order.service

import com.example.ecommerce.common.dto.cart.CartEvent
import com.example.ecommerce.common.dto.order.OrderStatusEnum
import com.example.ecommerce.order.model.Order
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface OrderService {
    fun createOrder(cartEvent: CartEvent): Order?
    fun findOrderById(orderId: Long): Order?
    fun findOrderByUserId(userId: Long): Order?
    fun updateOrderStatus(orderId: Long, status: OrderStatusEnum): Order?
    fun deleteOrder(orderId: Long)
    fun checkOrderAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean
    fun login(username: String, password: String): String?
}