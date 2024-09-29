package com.example.ecommerce.customer.service

import com.example.ecommerce.common.dto.customer.*
import com.example.ecommerce.customer.client.KeycloakClient
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CustomerServiceImpl(@Autowired private val customerRepository: CustomerRepository,
                          private val keycloakClient: KeycloakClient,
                          @Autowired @Qualifier("keycloakCustomerEventTemplate") private val keycloakCustomerEventTemplate: KafkaTemplate<String, KeycloakCustomerEventDto>,
) : CustomerService {

    @Transactional
    override fun registerCustomer(customerRegistration: Customer): Customer {
        if(customerRepository.findByUsername(customerRegistration.username) != null) {
            throw RuntimeException("Customer with username ${customerRegistration.username} already exists")
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
    override fun updateCustomer(updatedCustomer: Customer): Customer? {
        // Check for customer existence
        val customer = customerRepository.findByUsername(updatedCustomer.username) ?: throw RuntimeException("Customer (ID: ${updatedCustomer.id} not found")
        // Update customer data
        customer.email = updatedCustomer.email
        customer.username = updatedCustomer.username
        customer.password = updatedCustomer.password
        customer.address = updatedCustomer.address
        customer.shippingAddresses = updatedCustomer.shippingAddresses
        val keycloakCustomer = KeycloakCustomerDTO(username =  updatedCustomer.username, password =  updatedCustomer.password, email =  updatedCustomer.email)
        // Return updated customer
        try {
            keycloakCustomerEventTemplate.send("customer_keycloak_events", KeycloakCustomerEventDto("UPDATE", keycloakCustomer))
            return customerRepository.save(customer)
        }catch (ex: Exception){
            throw RuntimeException("Error during update of customer ${customer.username}: ${ex.message}")
        }
    }

    override fun loginCustomer(username: String, password: String): String?{
        val jwt = keycloakClient.loginCustomer(username, password)
        return jwt
    }

    @Transactional
    override fun deleteCustomer(id: Long) {
        // Find customer registered account
        val customer = customerRepository.findByIdOrNull(id) ?: throw RuntimeException("Customer (ID: $id not found)")
        // Delete customer
        try {
            val keycloakCustomer = KeycloakCustomerDTO(username =  customer.username, password =  customer.password, email =  customer.email)
            keycloakCustomerEventTemplate.send("customer_keycloak_events", KeycloakCustomerEventDto("DELETE", keycloakCustomer))
            customerRepository.delete(customer)
        }catch (ex: Exception){
            throw RuntimeException("Error during update of customer ${customer.username}: ${ex.message}")
        }
    }

    override fun getCustomerById(id: Long): Customer? {
        // Search for the customer with the given id, throw exception if not found
        return customerRepository.findByIdOrNull(id)?: throw RuntimeException("Customer (ID: $id not found)")
    }

    override fun getCustomerDTO(id: Long): CustomerDTO {
        // Search for the customer with the given id, throw exception if not found
        val customer = customerRepository.findByIdOrNull(id) ?: throw RuntimeException("Customer (ID: $id) not found")

        // Convert Customer's Address to AddressDTO
        val addressDTO: AddressDTO? = customer.address?.let {
            AddressDTO(
                it.addressCountry,
                it.addressRegion,
                it.addressLocality,
                it.postalCode,
                it.streetAddress
            )
        }

        // Convert Customer's Shipping Addresses to ShippingAddressDTO
        val shippingAddressesDTO: List<ShippingAddressDTO> = customer.shippingAddresses.map {
            ShippingAddressDTO(
                it.shippingAddressId,
                it.recipientName,
                AddressDTO(it.address.addressCountry,it.address.addressRegion,it.address.addressLocality,it.address.postalCode,it.address.streetAddress)
            )
        }

        // Create CustomerDTO
        return CustomerDTO(
            customer.id,
            customer.username,
            customer.email,
            customer.givenName,
            customer.familyName,
            addressDTO,
            shippingAddressesDTO,
            customer.createdAt,
            customer.updatedAt
        )
    }

    override fun checkCustomerAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        if(!roles.contains("CUSTOMER_MANAGER")){
            val customer = customerRepository.findByUsername(username)?: throw RuntimeException("You are not authorized to perform this operation")
            return customer.id == customerId && roles.contains("CUSTOMER")
        }
        return true
    }
}