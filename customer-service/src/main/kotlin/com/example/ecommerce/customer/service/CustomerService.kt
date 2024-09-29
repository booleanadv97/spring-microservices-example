package com.example.ecommerce.customer.service

import com.example.ecommerce.common.dto.customer.CustomerDTO
import com.example.ecommerce.customer.model.Customer
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

interface CustomerService {
    fun registerCustomer(customerRegistration: Customer): Customer
    fun updateCustomer(updatedCustomer: Customer): Customer?
    fun deleteCustomer(id: Long)
    fun getCustomerById(id: Long): Customer?
    fun getCustomerDTO(id: Long): CustomerDTO?
    fun loginCustomer(username: String, password: String): String?
    fun checkCustomerAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean
}