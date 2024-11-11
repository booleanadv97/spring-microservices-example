package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.model.Cart
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface CartService {
    fun create(authentication: JwtAuthenticationToken, customerId: Long): Cart
    fun addItem(authentication: JwtAuthenticationToken, customerId: Long, productId: Long, quantity: Int): Cart
    fun find(authentication: JwtAuthenticationToken, customerId: Long): Cart?
    fun clear(authentication: JwtAuthenticationToken, customerId: Long): String
    fun checkout(authentication: JwtAuthenticationToken, customerId: Long, shippingAddressId: Long): String
    fun removeItem(authentication: JwtAuthenticationToken, customerId: Long, productId: Long): Cart
}