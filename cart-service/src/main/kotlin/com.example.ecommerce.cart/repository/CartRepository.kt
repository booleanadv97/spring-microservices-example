package com.example.ecommerce.cart.repository

import com.example.ecommerce.cart.model.Cart
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository


@Repository
interface CartRepository : JpaRepository<Cart, Long>, QuerydslPredicateExecutor<Cart> {
    fun findByUserId(userId: Long): Cart?
}

