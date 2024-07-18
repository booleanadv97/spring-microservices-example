package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.model.Customer

interface CustomerService {
    fun registerCustomer(customerDto: CustomerDto): Customer?
    fun updateCustomer(customerDto: CustomerDto): Customer?
    fun deleteCustomer(id: Long)
    fun getCustomerById(id: Long): Customer?
    fun getCustomerByEmail(email: String): Customer?
}