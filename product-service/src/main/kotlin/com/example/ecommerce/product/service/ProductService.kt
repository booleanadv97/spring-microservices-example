package com.example.ecommerce.product.service

import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.model.Product

interface ProductService {
    fun getAllProducts(): List<Product>
    fun createProduct(productDto: ProductDto): Product
    fun updateProduct(id: Long, productDto: ProductDto): Product
    fun deleteProduct(id: Long)
    fun getProductById(id: Long): Product
    fun getProductByName(name: String): Product?
}