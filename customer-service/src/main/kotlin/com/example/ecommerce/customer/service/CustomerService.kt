package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.model.Customer

interface CustomerService {
    fun registerCustomer(username: String?, email: String?, password: String?): Customer?
    fun updateCustomer(id: Long?, username: String?, email: String?): Customer?
    fun deleteCustomer(id: Long?)
    fun getCustomerById(id: Long?): Customer?
    fun getCustomerByEmail(email: String?): Customer?
}