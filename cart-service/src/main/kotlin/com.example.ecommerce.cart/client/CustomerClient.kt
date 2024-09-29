package com.example.ecommerce.cart.client

import com.example.ecommerce.common.dto.customer.CustomerDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "customer-service")
interface CustomerClient {
    @GetMapping("/customers/api/{customerId}/DTO")
    fun getCustomerDTO(
        @PathVariable("customerId") customerId: Long
    ): CustomerDTO
}