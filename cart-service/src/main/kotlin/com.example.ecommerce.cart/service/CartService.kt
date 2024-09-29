package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.model.Cart
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface CartService {
    fun createCart(userId: Long): Cart
    fun addItemToCart(userId: Long, productId: Long, quantity: Int): Cart
    fun getCartByUserId(userId: Long): Cart?
    fun clearCart(userId: Long): String
    fun checkoutCart(userId: Long, shippingAddressId: Long): String
    fun removeItemFromCart(userId: Long, productId: Long): Cart
    fun checkCartAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean
}