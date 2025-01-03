package com.example.ecommerce.cart.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import feign.codec.ErrorDecoder

@Configuration
class FeignConfig {

    @Bean
    fun errorDecoder(): ErrorDecoder {
        return CustomFeignErrorDecoder()
    }
}
