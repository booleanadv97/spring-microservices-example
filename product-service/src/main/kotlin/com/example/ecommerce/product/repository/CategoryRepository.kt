package com.example.ecommerce.product.repository

import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository


@Repository
interface CategoryRepository : JpaRepository<Category, Long>, QuerydslPredicateExecutor<Product> {
    fun findAllBy(): List<Category>
    fun findByName(name: String): Category?
}
