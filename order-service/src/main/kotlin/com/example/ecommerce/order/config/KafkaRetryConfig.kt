package com.example.ecommerce.order.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.support.RetryTemplate

@Configuration
@EnableRetry
class KafkaRetryConfig {

    @Bean
    fun retryTemplate(): RetryTemplate {
        return RetryTemplate.builder()
            .maxAttempts(10)
            .fixedBackoff(2000)
            .build()
    }
}