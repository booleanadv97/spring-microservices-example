package com.example.ecommerce.cart.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(name = "inventory-service")
interface InventoryClient {
    @GetMapping("/inventory/api/check/{productId}")
    fun checkAvailability(
        @PathVariable("productId") productId: Long?,
        @RequestParam("quantity") quantity: Int
    ): Boolean?
}