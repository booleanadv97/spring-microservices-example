package com.example.ecommerce.order.config

import com.example.ecommerce.common.dto.cart.CartEvent
import com.example.ecommerce.common.dto.customer.KeycloakCustomerEventDto
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

    @Bean
    fun cartConsumerFactory(): ConsumerFactory<String, CartEvent> {
        val configProps: MutableMap<String, Any> = HashMap()

        // Basic consumer properties
        configProps[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaBootstrapServers
        configProps[ConsumerConfig.GROUP_ID_CONFIG] = kafkaConsumerGroupId
        configProps[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java.name
        configProps[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java.name
        val jsonDeserializer = JsonDeserializer(CartEvent::class.java).apply {
            addTrustedPackages("com.example.ecommerce.common.dto")
            setUseTypeMapperForKey(false)
        }
        return DefaultKafkaConsumerFactory(
            configProps,
            StringDeserializer(),
            jsonDeserializer
        )
    }

    @Bean
    fun cartKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, CartEvent> {
        val factory = ConcurrentKafkaListenerContainerFactory<String, CartEvent>()
        factory.consumerFactory = cartConsumerFactory()
        return factory
    }
}