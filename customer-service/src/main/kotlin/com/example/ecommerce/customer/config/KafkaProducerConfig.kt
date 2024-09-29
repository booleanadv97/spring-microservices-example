package com.example.ecommerce.customer.config

import com.example.ecommerce.common.dto.customer.KeycloakCustomerEventDto
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@EnableKafka
@Configuration
class KafkaProducerConfig {

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaBootstrapServers: String

    @Bean
    fun keycloakCustomerEventProducerFactory(): ProducerFactory<String, KeycloakCustomerEventDto> {
        val configProps: MutableMap<String, Any> = HashMap()

        // Basic producer properties
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java

        val jsonSerializer = JsonSerializer<KeycloakCustomerEventDto>().apply {
            setAddTypeInfo(false)
        }
        return DefaultKafkaProducerFactory(configProps, StringSerializer(), jsonSerializer)
    }

    @Bean(name = ["keycloakCustomerEventTemplate"])
    fun keycloakCustomerEventTemplate(): KafkaTemplate<String, KeycloakCustomerEventDto> {
        return KafkaTemplate(keycloakCustomerEventProducerFactory())
    }
}
