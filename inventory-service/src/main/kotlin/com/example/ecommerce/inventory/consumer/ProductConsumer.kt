package com.example.ecommerce.inventory.consumer

import com.example.ecommerce.common.dto.product.ProductEventDTO
import com.example.ecommerce.inventory.dto.StockDto
import com.example.ecommerce.inventory.service.InventoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class ProductConsumer(@Autowired val inventoryService: InventoryService) {
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["product_events"],
        containerFactory = "productKafkaListenerContainerFactory"
    )
    fun consume(productEvent: ProductEventDTO) {
        if(productEvent.eventType == "CREATE") {
            // Creates stock for newly created product
            val defaultStock = StockDto(productEvent.productDto.productId!!, 0, "default-warehouse")
            inventoryService.addStock(defaultStock)
        }
    }
}