package com.example.ecommerce.product.repository

import com.example.ecommerce.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    fun findByName(name: String): Product?
}