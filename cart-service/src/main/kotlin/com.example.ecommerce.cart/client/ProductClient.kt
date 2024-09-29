package com.example.ecommerce.cart.client

import com.example.ecommerce.common.dto.product.ProductDTO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "product-service")
interface ProductClient {
    @GetMapping("/products/api/{id}")
    fun getProductById(
        @PathVariable("id") productId: Long
    ): ProductDTO
}