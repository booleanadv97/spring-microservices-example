package com.example.ecommerce.inventory.service

import com.example.ecommerce.common.dto.order.OrderDTO
import com.example.ecommerce.common.dto.order.OrderEvent
import com.example.ecommerce.inventory.client.KeycloakClient
import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.repository.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class InventoryServiceImpl(@Autowired private var stockRepository: StockRepository,
    private val keycloakClient: KeycloakClient
): InventoryService {

    override fun getStockByProductId(productId: Long): Stock? {
        return stockRepository.findByProductId(productId)?:  throw RuntimeException("Stock for order with id $productId not found.")
    }

    @Transactional
    override fun updateStock(productId: Long, quantity: Int): Stock? {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for order (id $productId) not found.")
        stock.quantity = quantity
        return stockRepository.save(stock)
    }

    @Transactional
    override fun deleteStock(productId: Long) {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for the order (id $productId) not found.")
        stockRepository.delete(stock)
    }

    @Transactional
    override fun addStock(stockDto: StockDto): Stock {
        val stock = Stock(stockDto.productId, stockDto.productId, stockDto.quantity, stockDto.warehouse)
        return stockRepository.save(stock)
    }

    @Transactional
    override fun checkAvailability(productId: Long, quantity: Int): Boolean {
        val stock = stockRepository.findByProductId(productId) ?: throw RuntimeException("Stock for the order (id $productId) not found.")
        return stock.quantity >= quantity
    }

    override fun login(username: String, password: String): String? {
        val jwt = keycloakClient.loginInventory(username, password)
        return jwt
    }

    override fun getAllStocks(): List<Stock> {
        return stockRepository.findAll()
    }

    override fun newOrderInventoryOp(order: OrderDTO){
        val items = order.items
        items.forEach{
            item ->
            val tempStock = stockRepository.findByProductId(item.cartItem.product.productId!!) ?: throw RuntimeException("Error while executing inventory operation for new order (${order.id}")
            tempStock.quantity -= item.cartItem.quantity
            stockRepository.save(tempStock)
        }
    }
}
