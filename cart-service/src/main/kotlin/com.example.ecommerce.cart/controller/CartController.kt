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
@RequestMapping("/cart/api/v1")
class CartController {

    @Autowired
    private lateinit var cartService: CartService

    // Endpoint to create a new cart
    @Operation(summary = "Creates a new cart")
    @ApiResponse(responseCode = "200", description = "Cart created successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "409", description = "Cart already exists for customer with given id")
    // Create a new cart for the user
    @PostMapping("/{customerId}")
    fun create(authenticationToken: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<Cart> {
        val newCart = cartService.create(authenticationToken, customerId)
        return ResponseEntity.ok(newCart)
    }

    // Endpoint to add a new item to the cart
    @Operation(summary = "Add item to the cart")
    @ApiResponse(responseCode = "200", description = "Item added successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Cart or product not found")
    @ApiResponse(responseCode = "409", description = "Invalid quantity")
    @PostMapping("/{customerId}/items")
    fun addItem(
        authenticationToken: JwtAuthenticationToken,
        @PathVariable customerId: Long,
        @RequestParam productId: Long,
        @RequestParam quantity: Int,
    ): ResponseEntity<Cart> {
        val updatedCart = cartService.addItem(authenticationToken, customerId, productId, quantity)
        return ResponseEntity.ok(updatedCart)
    }

    // Endpoint to remove item from the cart
    @Operation(summary = "Removes item from cart")
    @ApiResponse(responseCode = "200", description = "Item removed successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Cart not found or product not present in the cart")
    @DeleteMapping("/{customerId}/items/{productId}")
    fun removeItem(
        authenticationToken: JwtAuthenticationToken,
        @PathVariable customerId: Long,
        @PathVariable productId: Long,
    ): ResponseEntity<Cart> {
        val updatedCart = cartService.removeItem(authenticationToken, customerId, productId)
        return ResponseEntity.ok(updatedCart)
    }

    // Endpoint to get the cart of a specific user
    @Operation(summary = "Get cart of a specific user")
    @ApiResponse(responseCode = "200", description = "Cart retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Cart not found for customer with given id")
    @GetMapping("/{customerId}")
    fun find(authenticationToken: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<Cart> {
        val cart = cartService.find(authenticationToken, customerId)
        return ResponseEntity.ok(cart)
    }

    // Endpoint to empty the cart of a specific user
    @Operation(summary = "Empty cart of a specific user")
    @ApiResponse(responseCode = "200", description = "Cart emptied successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Cart not found for customer with given id")
    @DeleteMapping("/{customerId}/items")
    fun clear(authenticationToken: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<String> {
        val result = cartService.clear(authenticationToken, customerId)
        return ResponseEntity.ok(result)
    }

    // Cart checkout endpoint
    @Operation(summary = "Cart checkout")
    @ApiResponse(responseCode = "200", description = "Checkout successfully")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Cart or shipping address not found for customer with given id")
    @PostMapping("/{customerId}/checkout")
    fun checkout(authenticationToken: JwtAuthenticationToken, @PathVariable customerId: Long, @RequestParam shippingAddressId: Long): ResponseEntity<String> {
        val result = cartService.checkout(authenticationToken, customerId, shippingAddressId)
        return ResponseEntity.ok(result)
    }
}