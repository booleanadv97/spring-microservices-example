package com.example.ecommerce.inventory.controller

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.service.InventoryServiceImpl
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/inventory")
class InventoryController(private val inventoryService: InventoryServiceImpl) {

    // Endpoint to find stock by id
    @GetMapping("/{productId}")
    @Operation(summary = "Find stock by product id")
    @ApiResponse(responseCode = "200", description = "Successful stock retrieval")
    fun getStockByProductId(@PathVariable productId: Long): ResponseEntity<Stock> {
        val stock = inventoryService.getStockByProductId(productId)
        return ResponseEntity(stock, HttpStatus.OK)
    }

    // Endpoint to update stock
    @Operation(summary = "Update stock")
    @ApiResponse(responseCode = "200", description = "Successful update")
    @PutMapping("/{productId}")
    fun updateStock(@PathVariable productId: Long, @RequestBody quantity: Int): ResponseEntity<Stock> {
        val updatedStock = inventoryService.updateStock(productId, quantity)
        return ResponseEntity(updatedStock, HttpStatus.OK)
    }

    // Endpoint to add a new stock
    @Operation(summary = "Add stock")
    @ApiResponse(responseCode = "200", description = "Successful stock add")
    @PostMapping("/register")
    fun addStock(@RequestBody stockDto: StockDto): ResponseEntity<Stock> {
        val newStock = inventoryService.addStock(stockDto)
        return ResponseEntity(newStock, HttpStatus.CREATED)
    }

    // Endpoint to delete stock
    @Operation(summary = "Delete stock")
    @ApiResponse(responseCode = "200", description = "Successful stock deletion")
    @DeleteMapping("/{productId}")
    fun deleteStock(@PathVariable productId: Long): ResponseEntity<Void> {
        inventoryService.deleteStock(productId)
        return ResponseEntity(HttpStatus.OK)
    }

    // Endpoint to get all stocks
    @Operation(summary = "Get all stocks")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of stocks")
    @GetMapping("/")
    fun getAllStocks(): ResponseEntity<List<Stock>> {
        val stocks = inventoryService.getAllStocks()
        return ResponseEntity(stocks, HttpStatus.OK)
    }
}
