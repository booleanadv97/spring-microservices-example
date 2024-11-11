package com.example.ecommerce.order.consumer

import com.example.ecommerce.order.dto.cart.CartEvent
import com.example.ecommerce.order.service.OrderService
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CartConsumer (private val orderService: OrderService){
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["cart_events"],
        containerFactory = "cartKafkaListenerContainerFactory"
    )
    fun consume(cartEvent: CartEvent) {
        if(cartEvent.eventType == "CHECKOUT")
            orderService.create(cartEvent)
    }
}