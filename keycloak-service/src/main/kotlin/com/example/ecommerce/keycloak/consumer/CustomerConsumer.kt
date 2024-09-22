package com.example.ecommerce.keycloak.consumer

import com.example.ecommerce.customer.dto.CustomerRegistration
import com.example.ecommerce.keycloak.service.KeycloakService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
class CustomerConsumer(@Autowired val keycloakService: KeycloakService) {
    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["register_customer"]
    )
    fun consumeRegister(customerRegistration: CustomerRegistration) {
        try {
            keycloakService.registerCustomer(customerRegistration)
        } catch (e: Exception) {
            println("Error processing registration for user ${customerRegistration.customer.username}: ${e.message}")
        }
    }

    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["update_customer"]
    )
    fun consumeUpdate(customerRegistration: CustomerRegistration) {
        try {
            keycloakService.updateCustomer(customerRegistration)
        } catch (e: Exception) {
            println("Error processing update for user ${customerRegistration.customer.username}: ${e.message}")
        }
    }

    @KafkaListener(
        groupId = "\${spring.kafka.consumer.group-id}",
        topics = ["delete_customer"]
    )
    fun consumeDelete(customerRegistration: CustomerRegistration) {
        try {
            keycloakService.removeUser(customerRegistration.customer.username)
        } catch (e: Exception) {
            println("Error processing deletion for user ${customerRegistration.customer.username}: ${e.message}")
        }
    }
}