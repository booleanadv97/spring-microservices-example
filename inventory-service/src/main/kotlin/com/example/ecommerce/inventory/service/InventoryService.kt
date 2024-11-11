package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.dto.order.OrderDTO
import com.example.ecommerce.inventory.model.Stock

interface InventoryService {
    fun find(productId: Long): Stock?
    fun update(productId: Long, quantity: Int): Stock?
    fun create(productId: Long, stockDto: StockDto): Stock
    fun delete(productId: Long)
    fun findAll(): List<Stock>?
    fun checkAvailability(productId: Long, quantity: Int):Boolean
    fun newOrderInventoryOp(order: OrderDTO)
}