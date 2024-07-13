package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service


@Service
class CustomerServiceImpl : CustomerService {

    @Autowired
    @Qualifier("customerRepository")
    private val customerRepository: CustomerRepository ? = null

    override fun registerCustomer(username: String?, email: String?, password: String?): Customer {
        // Check for null arguments
        if (username.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank()) {
            throw IllegalArgumentException("Username, email, and password must not be null or empty")
        }
        // Check if the customer already exists with the same email
        if (customerRepository!!.findByEmail(email) != null) {
            throw IllegalArgumentException("User with this email already exists")
        }
        // Create a new Customer object
        val newCustomer = Customer(
            username = username,
            email = email,
            password = password //TODO Remember to hash passwords
        )
        // Save the new customer to the database
        val savedCustomer = customerRepository.save(newCustomer)
        return savedCustomer
    }

    override fun updateCustomer(id: Long?, username: String?, email: String?): Customer {
        id ?: throw IllegalArgumentException("User id must not be null")
        // Fetch customer from repository
        val customer = customerRepository!!.findById(id)
            .orElseThrow { throw NoSuchElementException("User with id $id not found") }

        // Update customer fields if not null
        username?.let { customer.username = it }
        email?.let { customer.email = it }

        // Save updated customer
        val updatedCustomer = customerRepository.save(customer)
        return updatedCustomer
    }

    override fun deleteCustomer(id: Long?) {
        // Check for null argument
        id ?: throw IllegalArgumentException("User id must not be null")
        // Find customer registered account otherwise launch exception
        val customer = customerRepository!!.findById(id)
            .orElseThrow { throw NoSuchElementException("Customer with id $id not found") }
        // Delete customer from repository
        customerRepository.delete(customer)
    }

    override fun getCustomerById(id: Long?): Customer? {
        // Check for null argument
        id ?: throw IllegalArgumentException("Customer id must not be null")
        // Search for the customer with the given id, throw exception if not found
        val customer = customerRepository!!.findById(id)
            .orElseThrow { throw NoSuchElementException("Customer with id $id not found") }
        return customer
    }

    override fun getCustomerByEmail(email: String?): Customer? {
        // Check for null argument
        email ?: throw IllegalArgumentException("Email must not be null")
        // Search for customer with the given e-mail, if not found throw exception
        val customer = customerRepository!!.findByEmail(email)
        customer ?: throw NoSuchElementException("Customer with email $email not found")
        return customer
    }
}