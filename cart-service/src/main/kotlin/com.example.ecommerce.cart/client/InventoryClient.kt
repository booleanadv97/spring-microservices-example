package com.example.ecommerce.cart.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(name = "inventory-service")
interface InventoryClient {
    @PostMapping("/inventory/api/v1/availability/{productId}")
    fun checkAvailability(
        @PathVariable("productId") productId: Long?,
        @RequestBody quantity: Int
    ): Boolean?
}