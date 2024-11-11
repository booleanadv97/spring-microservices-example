package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.model.Customer
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface CustomerService {
    fun create(customerRegistration: Customer): Customer
    fun update(authentication: JwtAuthenticationToken, customerId: Long, updatedCustomer: Customer): Customer?
    fun delete(authentication: JwtAuthenticationToken, customerId: Long)
    fun findCustomer(authentication: JwtAuthenticationToken, customerId: Long): Customer?
}