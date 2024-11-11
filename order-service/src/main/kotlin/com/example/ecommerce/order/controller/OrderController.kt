package com.example.ecommerce.order.controller

import com.example.ecommerce.order.model.Order
import com.example.ecommerce.order.model.OrderStatusEnum
import com.example.ecommerce.order.service.OrderService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.ws.rs.NotAuthorizedException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders/api/v1")
class OrderController(private val orderService: OrderService) {

    // Endpoint to find order by id
    @GetMapping("/{orderId}")
    @Operation(summary = "Find order by id")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful order retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized")
    @ApiResponse(responseCode = "404", description = "Order with given id not found")
    fun find(@PathVariable orderId: Long): ResponseEntity<Order> {
        val order = orderService.find(orderId)
        return ResponseEntity.ok(order)
    }

    // Endpoint to find all orders
    @GetMapping("/")
    @Operation(summary = "Find all orders")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful orders retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized")
    fun findAll(): ResponseEntity<List<Order>> {
        val orders = orderService.findAll()
        return ResponseEntity.ok(orders)
    }

    // Endpoint to find customer orders
    @GetMapping("/customers/{customerId}")
    @Operation(summary = "Find orders by customer id")
    @ApiResponse(responseCode = "200", description = "Successful order retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized")
    fun findOrders(authenticationToken: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<List<Order>?> {
        if(!orderService.checkOrderAuth(authenticationToken, customerId))
            throw NotAuthorizedException("You are not authorized to perform this operation")
        val orders = orderService.findOrders(customerId)
        return ResponseEntity.ok(orders)
    }

    // Endpoint to update the status of an order by id (only for managers)
    @PutMapping("/{orderId}")
    @Operation(summary = "Update the status of an order")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Order successfully deleted")
    @ApiResponse(responseCode = "404", description = "Order with given id not found")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized")
    fun update(@PathVariable orderId: Long, @RequestBody status: OrderStatusEnum): ResponseEntity<Order> {
        val order = orderService.update(orderId, status)
        return ResponseEntity.ok(order)
    }

    // Endpoint to delete an order.
    @DeleteMapping("/{orderId}")
    @Operation(summary = "Delete order")
    @PreAuthorize("hasRole('ORDER_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Order successfully deleted")
    @ApiResponse(responseCode = "404", description = "Order with given id not found")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized")
    fun delete(@PathVariable orderId: Long): ResponseEntity<String> {
        orderService.delete(orderId)
        return ResponseEntity.ok("Order successfully deleted")
    }
}