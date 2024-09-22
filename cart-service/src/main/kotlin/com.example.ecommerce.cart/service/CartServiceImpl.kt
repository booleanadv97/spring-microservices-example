package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.client.InventoryClient
import com.example.ecommerce.cart.model.Cart
import com.example.ecommerce.cart.model.CartItem
import com.example.ecommerce.cart.repository.CartRepository
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CartServiceImpl(@Autowired private val cartRepository: CartRepository,
                      private val inventoryClient: InventoryClient) : CartService {
    // Create a new cart for the user
    override fun createCart(userId: Long): Cart {
        // Check if the user already has an existing cart
        val existingCart = cartRepository.findByUserId(userId)
        if (existingCart != null) {
            throw RuntimeException("Cart already exists for user $userId")
        }
        val newCart = Cart(userId = userId)
        return cartRepository.save(newCart)
    }

    // Add Item to Cart
    @Transactional
    override fun addItemToCart(userId: Long, productId: Long, quantity: Int): Cart {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        val isAvailable = inventoryClient.checkAvailability(productId, quantity);
        if (isAvailable == true) {
            val existingItem = cart.items.find { it.productId == productId }
            if (existingItem != null) {
                existingItem.quantity += quantity
            } else {
                cart.items.add(CartItem(productId = productId, quantity = quantity))
            }
            return cartRepository.save(cart)
        }else{
            throw RuntimeException("Product $productId not available")
        }
    }

    // Get Cart
    override fun getCartByUserId(userId: Long): Cart? = cartRepository.findByUserId(userId)

    // Clear Cart
    @Transactional
    override fun clearCart(userId: Long): String {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        cart.items.clear()
        cartRepository.save(cart)
        return "Cart cleared for user $userId"
    }

    // Checkout Cart
    @Transactional
    override fun checkoutCart(userId: Long): String {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")

        // kafkaTemplate.send("cart-events", CartEvent(eventType = "CHECKOUT", userId = userId, products = cart.items.map { it.productId }, quantities = cart.items.map { it.quantity }))

        cartRepository.delete(cart)
        return "Checkout completed for user $userId"
    }
}