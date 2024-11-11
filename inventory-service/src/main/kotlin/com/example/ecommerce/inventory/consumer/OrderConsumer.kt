package com.example.ecommerce.inventory.consumer

import com.example.ecommerce.inventory.dto.order.OrderEvent
import com.example.ecommerce.inventory.service.InventoryService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class OrderConsumer(@Autowired val inventoryService: InventoryService) {
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["order_events"],
        containerFactory = "orderKafkaListenerContainerFactory"
    )
    fun consume(orderEvent: OrderEvent) {
        if(orderEvent.eventType == "CREATE") {
            inventoryService.newOrderInventoryOp(orderEvent.order)
        }
    }
}