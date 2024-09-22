package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.CustomerRegistration
import com.example.ecommerce.customer.model.Customer

interface CustomerService {
    fun registerCustomer(customerRegistration: CustomerRegistration): Customer
    fun updateCustomer(customerId : Long, updatedCustomer: CustomerRegistration): Customer?
    fun deleteCustomer(id: Long)
    fun getCustomerById(id: Long): Customer?
}