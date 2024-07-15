package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.repository.StockRepository
import com.example.ecommerce.inventory.exception.InvalidParameterException
import com.example.ecommerce.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient


@Service
class InventoryServiceImpl(private val webClient: WebClient): InventoryService {

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
        // Check if stock exists
        if (stock != null) {
            stockRepository.delete(stock)
        }else{
            throw InvalidParameterException("Stock for the product (id $productId) not found.")
        }
    }

    fun getProduct(productId: Long): Product {
        return webClient.get()
            .uri("http://product-service:8081/api/products/$productId")
            .retrieve()
            .bodyToMono(Product::class.java)
            .block() ?: throw InvalidParameterException("Product $productId not found.")
    }

    @Transactional
    override fun addStock(stockDto: StockDto): Stock {
        // Check if stock already exists for product
        if(stockRepository.findByProductId(stockDto.productId) != null)
            throw InvalidParameterException("Product with id ${stockDto.productId} already exists.")
        // Get product
        val product = getProduct(stockDto.productId)
        // Add new stock
        val stock = Stock(stockDto.productId, product, stockDto.quantity, stockDto.warehouse)
        return stockRepository.save(stock)
    }

    override fun getAllStocks(): List<Stock> {
        return stockRepository.findAll()
    }
}
