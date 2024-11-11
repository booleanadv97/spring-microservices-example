package com.example.ecommerce.inventory.service

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.dto.order.OrderDTO
import com.example.ecommerce.inventory.exception.ConflictException
import com.example.ecommerce.inventory.exception.NotFoundException
import com.example.ecommerce.inventory.model.Stock
import com.example.ecommerce.inventory.repository.StockRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class InventoryServiceImpl(@Autowired private var stockRepository: StockRepository): InventoryService {

    override fun find(productId: Long): Stock? {
        return stockRepository.findByProductId(productId)?:  throw NotFoundException("Stock for the product $productId not found.")
    }

    @Transactional
    override fun update(productId: Long, quantity: Int): Stock? {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw NotFoundException("Stock for the product $productId not found.")
        if(quantity < 0) throw ConflictException("Quantity cannot be negative!")
        stock.quantity = quantity
        return stockRepository.save(stock)
    }

    @Transactional
    override fun delete(productId: Long) {
        // Check if stock exists
        val stock = stockRepository.findByProductId(productId) ?: throw NotFoundException("Stock for the product $productId not found.")
        stockRepository.delete(stock)
    }

    @Transactional
    override fun create(productId: Long, stockDto: StockDto): Stock {
        val stock = Stock(productId = productId, quantity = stockDto.quantity, warehouse = stockDto.warehouse)
        return stockRepository.save(stock)
    }

    @Transactional
    override fun checkAvailability(productId: Long, quantity: Int): Boolean {
        val stock = stockRepository.findByProductId(productId) ?: throw NotFoundException("Stock for the product $productId not found.")
        if(quantity < 0) throw ConflictException("Quantity cannot be negative!")
        return stock.quantity >= quantity
    }

    override fun findAll(): List<Stock>? {
        return stockRepository.findAll()
    }

    @Transactional
    override fun newOrderInventoryOp(order: OrderDTO){
        val items = order.items
        items.forEach{
            item ->
            val tempStock = stockRepository.findByProductId(item.cartItem.product.productId) ?: throw ConflictException("Error while executing inventory operation for new order (${order.id}")
            tempStock.quantity -= item.cartItem.quantity
            stockRepository.save(tempStock)
        }
    }
}
