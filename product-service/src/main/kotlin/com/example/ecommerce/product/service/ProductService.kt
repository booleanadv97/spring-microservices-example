package com.example.ecommerce.product.service

import com.example.ecommerce.product.model.Product

interface ProductService {
    fun createProduct(name: String, description: String, price: Double, quantity: Int): Product
    fun updateProduct(id: Long, name: String, description: String, price: Double, quantity: Int): Product
    fun deleteProduct(id: Long)
    fun getProductById(id: Long): Product
    fun getProductByName(name: String): Product?
}