package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import com.example.ecommerce.product.exception.InvalidParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class CustomerServiceImpl(@Autowired private val customerRepository: CustomerRepository) : CustomerService {


    override fun registerCustomer(customerDto: CustomerDto): Customer {
        // Create a new Customer object
        val newCustomer = Customer(
            givenName = customerDto.givenName,
            familyName = customerDto.familyName,
            email = customerDto.email,
            password = customerDto.password, //TODO Remember to hash passwords
            address = customerDto.address
        )
        // Save the new customer to the database
        val savedCustomer = customerRepository.save(newCustomer)
        return savedCustomer
    }

    override fun updateCustomer(customerDto: CustomerDto): Customer? {
        // Check for customer existence
        val customer = customerRepository.findByIdOrNull(customerDto.id) ?: throw InvalidParameterException("Customer (ID: ${customerDto.id} not found")
        // Update customer data
        customer.email = customerDto.email
        customer.password = customerDto.password
        customer.address = customerDto.address
        customer.updatedAt = LocalDateTime.now()
        // Return updated customer
        return customerRepository.save(customer)
    }

    override fun deleteCustomer(id: Long) {
        // Find customer registered account otherwise launch exception
        val customer = customerRepository.findByIdOrNull(id) ?: throw InvalidParameterException("Customer (ID: $id not found)")
        // Delete customer from repository
        customerRepository.delete(customer)
    }

    override fun getCustomerById(id: Long): Customer? {
        // Search for the customer with the given id, throw exception if not found
        return customerRepository.findByIdOrNull(id)?: throw InvalidParameterException("Customer (ID: $id not found)")
    }

    override fun getCustomerByEmail(email: String): Customer? {
        // Search for the customer with the given email, throw exception if not found
        return customerRepository.findByEmail(email) ?: throw InvalidParameterException("Customer with email $email not found")
    }
}