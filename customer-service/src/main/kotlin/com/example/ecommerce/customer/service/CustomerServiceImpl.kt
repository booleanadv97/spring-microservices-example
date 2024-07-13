package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.model.Address
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import com.example.ecommerce.product.exception.InvalidParameterException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class CustomerServiceImpl : CustomerService {

    @Autowired
    @Qualifier("customerRepository")
    private val customerRepository: CustomerRepository ? = null

    override fun registerCustomer(customerDto: CustomerDto): Customer {
        if (customerRepository!!.findByEmail(customerDto.email) != null) {
            throw InvalidParameterException("Customer with email ${customerDto.email} already exists.")
        }
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
        val customer = customerRepository!!.findById(customerDto.id)
            .orElseThrow { InvalidParameterException("Customer with id ${customerDto.id} not found") }
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
        val customer = customerRepository!!.findById(id)
            .orElseThrow { InvalidParameterException("Customer with id $id not found") }
        // Delete customer from repository
        customerRepository.delete(customer)
    }

    override fun getCustomerById(id: Long): Customer? {
        // Search for the customer with the given id, throw exception if not found
        return customerRepository!!.findById(id)
            .orElseThrow { InvalidParameterException("Customer with id $id not found")  }
    }

    override fun getCustomerByEmail(email: String?): Customer? {
        // Check for null argument
        email ?: throw IllegalArgumentException("Email must not be null")
        return customerRepository!!.findByEmail(email) ?: throw InvalidParameterException("Customer with email $email not found")
    }
}