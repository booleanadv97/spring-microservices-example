package com.example.ecommerce.inventory.repository

import com.example.ecommerce.inventory.model.Stock
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : JpaRepository<Stock, Long> , QuerydslPredicateExecutor<Stock>{
    fun findByProductId(productId: Long): Stock?
}