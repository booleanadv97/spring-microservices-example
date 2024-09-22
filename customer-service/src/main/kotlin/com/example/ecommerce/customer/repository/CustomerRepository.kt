package com.example.ecommerce.customer.repository

import com.example.ecommerce.customer.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.stereotype.Repository


@Repository
interface CustomerRepository : JpaRepository<Customer, Long>, QuerydslPredicateExecutor<Customer> {
    fun findByEmail(email: String): Customer?
    fun findByUsername(username: String): Customer?
}

