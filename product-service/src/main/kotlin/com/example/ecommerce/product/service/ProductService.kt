package com.example.ecommerce.product.service

import com.example.ecommerce.common.dto.product.CategoryDTO
import com.example.ecommerce.common.dto.product.ProductDTO
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product

interface ProductService {
    fun getAllProducts(): List<Product>
    fun createProduct(productDto: ProductDTO): Product
    fun updateProduct(productDto: ProductDTO): Product
    fun getCategories(): List<CategoryDTO>
    fun createCategory(categoryDto: CategoryDTO): Category
    fun deleteProduct(productId: Long)
    fun getProductById(productId: Long): ProductDTO
    fun getProductByName(name: String): ProductDTO?
    fun login(username: String, password: String): String?
}