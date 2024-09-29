package com.example.ecommerce.order.controller

import com.example.ecommerce.common.dto.order.OrderStatusEnum
import com.example.ecommerce.order.model.Order
import com.example.ecommerce.order.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders/api")
class OrderController(private val orderService: OrderService) {

    // Endpoint to login
    @PostMapping("/login")
    @Operation(summary = "Login as order manager")
    @ApiResponse(responseCode = "200", description = "Successful login")
    fun login(@RequestParam username : String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = orderService.login(username, password)
        return ResponseEntity.ok(jwt)
    }

    // Endpoint to find order by id
    @GetMapping("/{orderId}")
    @Operation(summary = "Find order by id")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful order retrieval")
    fun findOrderById(@PathVariable orderId: Long): ResponseEntity<Order> {
        val order = orderService.findOrderById(orderId)
        return ResponseEntity.ok(order)
    }

    // Endpoint to find order by user id
    @GetMapping("/users/{userId}")
    @Operation(summary = "Find order by user id")
    @ApiResponse(responseCode = "200", description = "Successful order retrieval")
    fun findOrderByUserId(authenticationToken: JwtAuthenticationToken, @PathVariable userId: Long): ResponseEntity<Order> {
        if(!orderService.checkOrderAuth(authenticationToken, userId))
            throw RuntimeException("You are not authorized to perform this operation")
        val order = orderService.findOrderByUserId(userId)
        return ResponseEntity.ok(order)
    }

    // Endpoint to update the status of an order by id (only for managers)
    @PutMapping("/{orderId}/status")
    @Operation(summary = "Update the status of an order")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful order status update")
    fun updateOrderStatusById(@PathVariable orderId: Long, @RequestParam status: OrderStatusEnum): ResponseEntity<Order> {
        val order = orderService.updateOrderStatus(orderId, status)
        return ResponseEntity.ok(order)
    }


    // Endpoint to delete an order.
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Order successfully deleted")
    fun deleteOrder(@PathVariable orderId: Long): ResponseEntity<String> {
        orderService.deleteOrder(orderId)
        return ResponseEntity.ok("Order successfully deleted")
    }
}