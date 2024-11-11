package com.example.ecommerce.order.repository

import com.example.ecommerce.order.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : JpaRepository<Order, Long>, QuerydslPredicateExecutor<Order> {
    fun findByCustomerId(customerId: Long): List<Order>?
}