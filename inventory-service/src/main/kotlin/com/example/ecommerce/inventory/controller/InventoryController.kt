package com.example.ecommerce.inventory.controller

import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.service.InventoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inventory/api/v1")
class InventoryController(private val inventoryService: InventoryService) {

    // Endpoint to find stock by product id
    @GetMapping("/{productId}")
    @Operation(summary = "Find stock by product id")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful stock retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Stock for product with given id not found")
    fun find(@PathVariable productId: Long): ResponseEntity<Stock> {
        val stock = inventoryService.find(productId)
        return ResponseEntity.ok(stock)
    }

    // Endpoint to update stock
    @Operation(summary = "Update stock")
    @ApiResponse(responseCode = "200", description = "Successful stock availability retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Stock for product with given id not found")
    @ApiResponse(responseCode = "409", description = "Invalid quantity")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @PutMapping("/{productId}")
    fun update(@PathVariable productId: Long, @RequestBody quantity: Int): ResponseEntity<Stock> {
        val updatedStock = inventoryService.update(productId, quantity)
        return ResponseEntity.ok(updatedStock)
    }

    @PostMapping("/availability/{productId}")
    @PreAuthorize("hasRole('INVENTORY_MANAGER') or hasRole('CUSTOMER')")
    @ApiResponse(responseCode = "200", description = "Successful stock availability retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @ApiResponse(responseCode = "404", description = "Stock for product with given id not found")
    @ApiResponse(responseCode = "409", description = "Invalid quantity")
    fun checkProductAvailability( @PathVariable productId: Long, @RequestBody quantity: Int): ResponseEntity<Boolean> {
        val isAvailable = inventoryService.checkAvailability(productId, quantity)
        return ResponseEntity.ok(isAvailable)
    }

    // Endpoint to find all stocks
    @Operation(summary = "Get all stocks")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of stocks")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this action")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @GetMapping("/")
    fun findAll(authentication: JwtAuthenticationToken): ResponseEntity<List<Stock>?> {
        val stocks = inventoryService.findAll()
        return ResponseEntity.ok(stocks)
    }
}
