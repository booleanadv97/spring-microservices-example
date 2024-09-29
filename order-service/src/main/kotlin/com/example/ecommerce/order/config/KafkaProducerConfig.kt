package com.example.ecommerce.order.config

import com.example.ecommerce.common.dto.order.OrderEvent
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
class KafkaProducerConfig{

    @Value("\${spring.kafka.bootstrap-servers}")
    private lateinit var kafkaBootstrapServers: String

    @Bean
    fun orderEventProducerFactory(): ProducerFactory<String, OrderEvent> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapServers
        configProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        configProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = JsonSerializer::class.java
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean(name = ["orderEventTemplate"])
    fun orderEventTemplate(): KafkaTemplate<String, OrderEvent> {
        return KafkaTemplate(orderEventProducerFactory())
    }
}