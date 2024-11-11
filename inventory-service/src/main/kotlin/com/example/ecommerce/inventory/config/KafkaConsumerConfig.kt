package com.example.ecommerce.inventory.config

import com.example.ecommerce.inventory.dto.order.OrderEvent
import com.example.ecommerce.inventory.dto.product.ProductEventDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Value("\${spring.kafka.consumer.bootstrap-servers}")
    private lateinit var kafkaBootstrapServers: String

    @Value("\${spring.kafka.consumer.group-id}")
    private lateinit var kafkaConsumerGroupId: String

    // Product Consumer Factory
    @Bean
    fun productConsumerFactory(): ConsumerFactory<String, ProductEventDTO> {
        val deserializer = JsonDeserializer(ProductEventDTO::class.java).apply {
            addTrustedPackages("*")
            ignoreTypeHeaders()
        }
        return DefaultKafkaConsumerFactory(configProps(), StringDeserializer(), deserializer)
    }

    @Bean
    fun productKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, ProductEventDTO> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, ProductEventDTO>()
        factory.consumerFactory = productConsumerFactory()
        return factory
    }

    // Order Consumer Factory
    @Bean
    fun orderConsumerFactory(): ConsumerFactory<String, OrderEvent> {
        val deserializer = JsonDeserializer(OrderEvent::class.java).apply {
            addTrustedPackages("*")
            ignoreTypeHeaders()
        }
        return DefaultKafkaConsumerFactory(configProps(), StringDeserializer(), deserializer)
    }

    @Bean
    fun orderKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, OrderEvent>()
        factory.consumerFactory = orderConsumerFactory()
        return factory
    }

    // Common Kafka Consumer Configurations
    fun configProps(): MutableMap<String, Any> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapServers
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = kafkaConsumerGroupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return configProps
    }
}