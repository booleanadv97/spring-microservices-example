package com.example.ecommerce.keycloak.consumer

import com.example.ecommerce.keycloak.dto.customer.KeycloakCustomerEventDto
import com.example.ecommerce.keycloak.service.KeycloakService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CustomerConsumer(@Autowired val keycloakService: KeycloakService) {
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["customer_keycloak_events"],
        containerFactory = "customerKafkaListenerContainerFactory"
    )
    fun listenToCustomerEvents(keycloakCustomerEventDto: KeycloakCustomerEventDto) {
        when (keycloakCustomerEventDto.eventType) {
            "CREATE" -> {
                try {
                    keycloakService.registerCustomer(keycloakCustomerEventDto.keycloakCustomer)
                } catch (e: Exception) {
                    println("Error processing registration for user ${keycloakCustomerEventDto.keycloakCustomer.username}: ${e.message}")
                }
            }
            "UPDATE" -> {
                try {
                    keycloakService.updateCustomer(keycloakCustomerEventDto.keycloakCustomer)
                } catch (e: Exception) {
                    println("Error processing update of user ${keycloakCustomerEventDto.keycloakCustomer.username}: ${e.message}")
                }
            }
            "DELETE" -> {
                try {
                    keycloakService.removeUser(keycloakCustomerEventDto.keycloakCustomer.username)
                } catch (e: Exception) {
                    println("Error processing deletion of user ${keycloakCustomerEventDto.keycloakCustomer.username}: ${e.message}")
                }
            }
        }
    }
}