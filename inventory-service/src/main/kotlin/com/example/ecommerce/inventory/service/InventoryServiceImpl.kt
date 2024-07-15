package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.repository.StockRepository
import com.example.ecommerce.inventory.exception.InvalidParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryServiceImpl: InventoryService {

    @Autowired
    private lateinit var stockRepository: StockRepository

    override fun getStockByProductId(productId: Long): Stock? {
        return stockRepository.findByProductId(productId)?:  throw InvalidParameterException("Product with id $productId not found.")
    }

    @Transactional
    override fun updateStock(productId: Long, quantity: Int): Stock? {
        val stock = stockRepository.findByProductId(productId)
        return if (stock != null) {
            stock.quantity = quantity
            stockRepository.save(stock)
        } else {
            throw InvalidParameterException("Product with id $productId not found.")
        }
    }

    @Transactional
    override fun deleteStock(productId: Long) {
        val stock = stockRepository.findByProductId(productId)
        if (stock != null) {
            stockRepository.delete(stock)
        }else{
            throw InvalidParameterException("Product with id $productId not found.")
        }
    }

    @Transactional
    override fun addStock(stock: Stock): Stock {
        return stockRepository.save(stock)
    }

    override fun getAllStocks(): List<Stock> {
        return stockRepository.findAll()
    }
}
