package com.example.ecommerce.product.service

import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product

interface ProductService {
    fun findAll(): List<Product>?
    fun create(product: Product): Product
    fun update(productId: Long, updatedProduct: Product): Product
    fun updateCategory(categoryId: Long, updatedCategory: Category): Category
    fun findCategories(): List<Category>?
    fun createCategory(category: Category): Category
    fun delete(productId: Long)
    fun find(productId: Long): Product?
    fun findByName(name: String): Product?
    fun deleteCategory(categoryId: Long)
    fun findCategory(categoryId: Long): Category?
}