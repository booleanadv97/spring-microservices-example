package com.example.ecommerce.inventory.consumer

import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.service.InventoryService
import com.example.ecommerce.product.model.Product
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ProductConsumer(@Autowired val inventoryService: InventoryService) {
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["create_default_stock_for_product"]
    )
    fun consume(product: Product) {
        // Creates stock for newly created product
        val defaultStock = StockDto(product.id,0,"default-warehouse")
        inventoryService.addStock(defaultStock)
    }
}