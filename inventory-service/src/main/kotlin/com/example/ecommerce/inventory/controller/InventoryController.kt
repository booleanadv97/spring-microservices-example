package com.example.ecommerce.inventory.controller

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.service.InventoryServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inventory/api")
class InventoryController(private val inventoryService: InventoryServiceImpl) {

    // Endpoint to find stock by id
    @GetMapping("/{productId}")
    @Operation(summary = "Find stock by product id")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful stock retrieval")
    fun getStockByProductId(@PathVariable productId: Long): ResponseEntity<Stock> {
        val stock = inventoryService.getStockByProductId(productId)
        return ResponseEntity.ok(stock)
    }

    // Endpoint to update stock
    @Operation(summary = "Update stock")
    @ApiResponse(responseCode = "200", description = "Successful update")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @PutMapping("/{productId}")
    fun updateStock(@PathVariable productId: Long, @RequestBody quantity: Int): ResponseEntity<Stock> {
        val updatedStock = inventoryService.updateStock(productId, quantity)
        return ResponseEntity.ok(updatedStock)
    }

    // Endpoint to add a new stock
    @Operation(summary = "Add stock")
    @ApiResponse(responseCode = "200", description = "Successful stock add")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @PostMapping("/register")
    fun addStock( @RequestBody stockDto: StockDto): ResponseEntity<Stock> {
        val newStock = inventoryService.addStock(stockDto)
        return ResponseEntity.ok(newStock)
    }

    @GetMapping("/check/{productId}")
    @PreAuthorize("hasRole('INVENTORY_MANAGER') or hasRole('CUSTOMER')")
    fun checkAvailability( @PathVariable productId: Long, @RequestParam quantity: Int): ResponseEntity<Boolean> {
        val isAvailable = inventoryService.checkAvailability(productId, quantity)
        return ResponseEntity.ok(isAvailable)
    }

    // Endpoint to delete stock
    @Operation(summary = "Delete stock")
    @ApiResponse(responseCode = "200", description = "Successful stock deletion")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @DeleteMapping("/{productId}")
    fun deleteStock(@PathVariable productId: Long): ResponseEntity<Void> {
        inventoryService.deleteStock(productId)
        return ResponseEntity.ok(null)
    }

    // Endpoint to get all stocks
    @Operation(summary = "Get all stocks")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of stocks")
    @PreAuthorize("hasRole('INVENTORY_MANAGER')")
    @GetMapping("/")
    fun getAllStocks(authentication: JwtAuthenticationToken): ResponseEntity<List<Stock>> {
        val stocks = inventoryService.getAllStocks()
        return ResponseEntity.ok(stocks)
    }
}
