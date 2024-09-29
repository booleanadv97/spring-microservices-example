package com.example.ecommerce.inventory.service

import com.example.ecommerce.common.dto.order.OrderDTO
import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock

interface InventoryService {
    fun getStockByProductId(productId: Long): Stock?
    fun updateStock(productId: Long, quantity: Int): Stock?
    fun addStock(stockDto: StockDto): Stock
    fun deleteStock(productId: Long)
    fun getAllStocks(): List<Stock>
    fun checkAvailability(productId: Long, quantity: Int):Boolean
    fun login(username: String, password: String): String?
    fun newOrderInventoryOp(order: OrderDTO)
}