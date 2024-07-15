package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.model.Stock

interface InventoryService {
    fun getStockByProductId(productId: Long): Stock?
    fun updateStock(productId: Long, quantity: Int): Stock?
    fun addStock(stock: Stock): Stock
    fun deleteStock(productId: Long)
    fun getAllStocks(): List<Stock>
}