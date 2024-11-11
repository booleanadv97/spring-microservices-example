package com.example.ecommerce.cart.client

import com.example.ecommerce.cart.dto.customer.CustomerDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "customer-service")
interface CustomerClient {
    @GetMapping("/customers/api/v1/{customerId}")
    fun findCustomer(
        @PathVariable("customerId") customerId: Long
    ): CustomerDTO
}