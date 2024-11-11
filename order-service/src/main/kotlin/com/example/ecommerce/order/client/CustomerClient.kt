package com.example.ecommerce.order.client

import com.example.ecommerce.order.dto.customer.CustomerDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "customer-service")
interface CustomerClient {
    @GetMapping("/customers/api/{customerId}/")
    fun findCustomer(
        @PathVariable("customerId") customerId: Long
    ): CustomerDTO
}