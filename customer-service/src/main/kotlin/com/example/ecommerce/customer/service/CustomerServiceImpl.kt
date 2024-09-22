package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.dto.CustomerRegistration
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CustomerServiceImpl(@Autowired private val customerRepository: CustomerRepository,
                          @Autowired private val kafkaTemplate: KafkaTemplate<String, CustomerRegistration>,
) : CustomerService {
    override fun registerCustomer(customerRegistration: CustomerRegistration): Customer {
        if(customerRepository.findByUsername(customerRegistration.customer.username) != null) {
            throw RuntimeException("Customer with username ${customerRegistration.customer.username} already exists")
        }
        // Create a new Customer object
        val newCustomer = Customer(
            username = customerRegistration.customer.username,
            givenName = customerRegistration.customer.givenName,
            familyName = customerRegistration.customer.familyName,
            email = customerRegistration.customer.email,
            password = customerRegistration.password,
            address = customerRegistration.customer.address
        )
        try {
            // Save the new customer to keycloak
            kafkaTemplate.send("register_customer", customerRegistration)
            return customerRepository.save(newCustomer)
        }catch (ex: Exception){
            throw RuntimeException("Error during creation of customer ${newCustomer.username}: ${ex.message}")
        }
    }

    override fun updateCustomer(customerId: Long, updatedCustomer: CustomerRegistration): Customer? {
        // Check for customer existence
        val customer = customerRepository.findByUsername(updatedCustomer.customer.username) ?: throw RuntimeException("Customer (ID: $customerId not found")
        // Update customer data
        customer.email = updatedCustomer.customer.email
        customer.password = updatedCustomer.password
        customer.address = updatedCustomer.customer.address
        customer.updatedAt = LocalDateTime.now()
        // Return updated customer
        try {
            kafkaTemplate.send("update_customer", updatedCustomer)
            return customerRepository.save(customer)
        }catch (ex: Exception){
            throw RuntimeException("Error during update of customer ${customer.username}: ${ex.message}")
        }
    }

    override fun deleteCustomer(id: Long) {
        // Find customer registered account otherwise launch exception
        val customer = customerRepository.findByIdOrNull(id) ?: throw RuntimeException("Customer (ID: $id not found)")
        // Delete customer
        try {
            val customerDeleted = CustomerRegistration(CustomerDto(customer.username, customer.email, customer.familyName, customer.givenName, customer.address),customer.password)
            kafkaTemplate.send("delete_customer", customerDeleted)
            customerRepository.delete(customer)
        }catch (ex: Exception){
            throw RuntimeException("Error during update of customer ${customer.username}: ${ex.message}")
        }
    }

    override fun getCustomerById(id: Long): Customer? {
        // Search for the customer with the given id, throw exception if not found
        return customerRepository.findByIdOrNull(id)?: throw RuntimeException("Customer (ID: $id not found)")
    }

}