package com.example.ecommerce.product.dto

import com.example.ecommerce.product.model.Category


data class ProductDto(
    val name: String,
    val description: String,
    val price: Double,
    val category: Category
)