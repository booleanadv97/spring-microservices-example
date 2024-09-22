package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.repository.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class InventoryServiceImpl(@Autowired private var stockRepository: StockRepository
): InventoryService {

    override fun getStockByProductId(productId: Long): Stock? {
        return stockRepository.findByProductId(productId)?:  throw RuntimeException("Stock for product with id $productId not found.")
    }

    @Transactional
    override fun updateStock(productId: Long, quantity: Int): Stock? {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for product (id $productId) not found.")
        stock.quantity = quantity
        return stockRepository.save(stock)
    }

    @Transactional
    override fun deleteStock(productId: Long) {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for the product (id $productId) not found.")
        stockRepository.delete(stock)
    }

    @Transactional
    override fun addStock(stockDto: StockDto): Stock {
        val stock = Stock(stockDto.productId, stockDto.productId, stockDto.quantity, stockDto.warehouse)
        return stockRepository.save(stock)
    }

    @Transactional
    override fun checkAvailability(productId: Long, quantity: Int): Boolean {
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for the product (id $productId) not found.")
        return stock.quantity >= quantity
    }

    override fun getAllStocks(): List<Stock> {
        return stockRepository.findAll()
    }
}
