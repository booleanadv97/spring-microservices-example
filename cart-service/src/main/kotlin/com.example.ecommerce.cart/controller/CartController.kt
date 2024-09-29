package com.example.ecommerce.cart.controller

import com.example.ecommerce.cart.model.Cart
import com.example.ecommerce.cart.service.CartService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/cart/api")
class CartController {

    @Autowired
    private lateinit var cartService: CartService

    // Endpoint to create a new cart
    @Operation(summary = "Creates a new cart")
    @ApiResponse(responseCode = "200", description = "Cart created successfully")
    // Create a new cart for the user
    @PostMapping("/{userId}/create")
    fun createCart(authenticationToken: JwtAuthenticationToken, @PathVariable userId: Long): ResponseEntity<Cart> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val newCart = cartService.createCart(userId)
        return ResponseEntity.ok(newCart)
    }

    // Endpoint to add a new item to the cart
    @Operation(summary = "Add item to the cart")
    @ApiResponse(responseCode = "200", description = "Item added successfully")
    @PostMapping("/{userId}/items")
    fun addItemToCart(
        authenticationToken: JwtAuthenticationToken,
        @PathVariable userId: Long,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val updatedCart = cartService.addItemToCart(userId, productId, quantity)
        return ResponseEntity.ok(updatedCart)
    }

    // Endpoint to remove item from the cart
    @Operation(summary = "Removes item from cart")
    @ApiResponse(responseCode = "200", description = "Item removed successfully")
    @DeleteMapping("/{userId}/clearItem")
    fun removeItemFromCart(
        authenticationToken: JwtAuthenticationToken,
        @PathVariable userId: Long,
        @RequestParam productId: Long,
    ): ResponseEntity<Cart> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val updatedCart = cartService.removeItemFromCart(userId, productId)
        return ResponseEntity.ok(updatedCart)
    }

    // Endpoint to get the cart of a specific user
    @Operation(summary = "Get cart of a specific user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @GetMapping("/{userId}")
    fun viewCart(authenticationToken: JwtAuthenticationToken, @PathVariable userId: Long): ResponseEntity<Cart> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val cart = cartService.getCartByUserId(userId) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(cart)
    }

    // Endpoint to delete the  cart of a specific user
    @Operation(summary = "Delete cart of a specific user")
    @ApiResponse(responseCode = "200", description = "Cart deleted successfully")
    @DeleteMapping("/{userId}/clear")
    fun clearCart(authenticationToken: JwtAuthenticationToken, @PathVariable userId: Long): ResponseEntity<String> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val result = cartService.clearCart(userId)
        return ResponseEntity.ok(result)
    }

    // Cart checkout endpoint
    @Operation(summary = "Cart checkout")
    @ApiResponse(responseCode = "200", description = "Checkout successfully")
    @PostMapping("/{userId}/checkout")
    fun checkoutCart(authenticationToken: JwtAuthenticationToken, @PathVariable userId: Long, @RequestParam shippingAddressId: Long): ResponseEntity<String> {
        if(!cartService.checkCartAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val result = cartService.checkoutCart(userId, shippingAddressId)
        return ResponseEntity.ok(result)
    }
}