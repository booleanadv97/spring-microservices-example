package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.model.Cart

interface CartService {
    fun createCart(userId: Long): Cart
    fun addItemToCart(userId: Long, productId: Long, quantity: Int): Cart
    fun getCartByUserId(userId: Long): Cart?
    fun clearCart(userId: Long): String
    fun checkoutCart(userId: Long): String
}