package com.example.ecommerce.customer.service

import com.example.ecommerce.customer.dto.KeycloakCustomerDTO
import com.example.ecommerce.customer.dto.KeycloakCustomerEventDto
import com.example.ecommerce.customer.exception.ConflictException
import com.example.ecommerce.customer.exception.NotFoundException
import com.example.ecommerce.customer.exception.UnauthorizedException
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

@Service
class CustomerServiceImpl(@Autowired private val customerRepository: CustomerRepository,
                          @Autowired @Qualifier("keycloakCustomerEventTemplate") private val keycloakCustomerEventTemplate: KafkaTemplate<String, KeycloakCustomerEventDto>,
) : CustomerService {

    @Transactional
    override fun create(customerRegistration: Customer): Customer {
        if(customerRepository.findByUsername(customerRegistration.username) != null) {
            throw ConflictException("Customer with username ${customerRegistration.username} already exists")
        }
        try {
            val keycloakCustomer = KeycloakCustomerDTO(givenName = customerRegistration.givenName, familyName = customerRegistration.familyName,
                username = customerRegistration.username, email = customerRegistration.email, password = customerRegistration.password)
            // Save the new customer to keycloak
            keycloakCustomerEventTemplate.send("customer_keycloak_events",
                KeycloakCustomerEventDto("CREATE", keycloakCustomer)
            )
            return customerRepository.save(customerRegistration)
        }catch (ex: Exception){
            throw RuntimeException("Error during creation of customer ${customerRegistration.username}: ${ex.message}")
        }
    }

    @Transactional
    override fun update(authentication: JwtAuthenticationToken, customerId: Long, updatedCustomer: Customer): Customer? {
        if (!checkCustomerAuth(authentication, customerId))
            throw UnauthorizedException("You are not authorized to perform this action")

        // Retrieve the existing customer
        val customer = customerRepository.findById(customerId).orElseThrow { NotFoundException("Customer $customerId not found") }
        val checkUpdatedUsername =  customerRepository.findByUsername(updatedCustomer.username)
        if(checkUpdatedUsername != null){
            if(checkUpdatedUsername.username != customer.username)
                throw ConflictException("Username already used")
        }
        // Send event to Keycloak
        val keycloakCustomer = KeycloakCustomerDTO(
            username = customer.username,
            updatedUsername = if (updatedCustomer.username == customer.username) null else updatedCustomer.username,
            password = updatedCustomer.password,
            email = updatedCustomer.email
        )

        // Update basic fields
        customer.email = updatedCustomer.email
        customer.username = updatedCustomer.username
        customer.password = updatedCustomer.password
        customer.address = updatedCustomer.address

        customer.shippingAddresses.clear()

        updatedCustomer.shippingAddresses.forEach {
            address ->
            customer.shippingAddresses.add(address)
        }

        // Send the event to Keycloak
        keycloakCustomerEventTemplate.send("customer_keycloak_events", KeycloakCustomerEventDto("UPDATE", keycloakCustomer))

        // Save and return the updated customer
        return customerRepository.save(customer)
    }

    @Transactional
    override fun delete(authentication: JwtAuthenticationToken, customerId: Long) {
        if(!checkCustomerAuth(authentication, customerId))
            throw UnauthorizedException("You are not authorized to perform this action")
        // Find customer registered account
        val customer = customerRepository.findById(customerId).getOrNull() ?: throw NotFoundException("Customer $customerId not found)")
        // Delete customer
        try {
            val keycloakCustomer = KeycloakCustomerDTO(username =  customer.username, password =  customer.password, email =  customer.email)
            keycloakCustomerEventTemplate.send("customer_keycloak_events", KeycloakCustomerEventDto("DELETE", keycloakCustomer))
            customerRepository.delete(customer)
        }catch (ex: Exception){
            throw RuntimeException("Error during deletion of customer ${customer.username}: ${ex.message}")
        }
    }

    override fun findCustomer(authentication: JwtAuthenticationToken, customerId: Long): Customer? {
        if(!checkCustomerAuth(authentication, customerId))
            throw UnauthorizedException("You are not authorized to perform this action")
        // Search for the customer with the given id, throw exception if not found
        return customerRepository.findById(customerId).getOrNull()?: throw NotFoundException("Customer $customerId not found)")
    }

    fun checkCustomerAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        if(!roles.contains("CUSTOMER_MANAGER")){
            val customer = customerRepository.findById(customerId).getOrNull()?: throw NotFoundException("Customer not present in customer service database.")
            return customer.username == username &&  roles.contains("CUSTOMER")
        }
        return true
    }
}