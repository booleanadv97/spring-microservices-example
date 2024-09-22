package com.example.ecommerce.customer.controller

import com.example.ecommerce.customer.dto.CustomerRegistration
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.repository.CustomerRepository
import com.example.ecommerce.customer.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers/api")
class CustomerController {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var customerService: CustomerService

    // Endpoint to register new customer
    @PostMapping("/register")
    @Operation(summary = "Register new customer")
    @ApiResponse(responseCode = "200", description = "Successful customer registration")
    fun registerCustomer(@RequestBody customerRegistration: CustomerRegistration): ResponseEntity<Customer> {
        val newCustomer = customerService.registerCustomer(customerRegistration)
        return ResponseEntity.ok(newCustomer)
    }

    // Endpoint to find customer by id
    @GetMapping("/{customerId}")
    @Operation(summary = "Find customer by ID")
    @ApiResponse(responseCode = "200", description = "Successful customer retrieval")
    fun getUserById(authentication: JwtAuthenticationToken,
                    @PathVariable customerId: Long): ResponseEntity<Customer> {
        val customer = customerService.getCustomerById(customerId)
        if(checkCustomerAuth(authentication, customerId)) {
            return ResponseEntity.ok(customer)
        }else{
            throw RuntimeException("You are not authorized to perform this operation")
        }
    }

    // Endpoint to update customer
    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Successful customer update")
    fun updateUser(authentication: JwtAuthenticationToken,
        @PathVariable customerId: Long,
        @RequestBody updatedCustomer: CustomerRegistration,
    ): ResponseEntity<Customer> {
        if(checkCustomerAuth(authentication, customerId)){
            val customer = customerService.updateCustomer(customerId, updatedCustomer)
            return ResponseEntity.ok(customer)
        }else{
            throw RuntimeException("You are not authorized to perform this operation")
        }
    }

    // Endpoint to delete user
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Successful customer deletion")
    @DeleteMapping("/{customerId}")
    fun deleteUser(authentication: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<Void> {
        if(checkCustomerAuth(authentication,customerId)) {
            customerService.deleteCustomer(customerId)
            return ResponseEntity.ok(null)
        }else{
            throw RuntimeException("You are not authorized to perform this operation")
        }
    }

    fun checkCustomerAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        if(!roles.contains("CUSTOMER_MANAGER")){
            customerRepository.findByUsername(username)?: throw RuntimeException("You are not authorized to perform this operation")
            return customerRepository.findByUsername(username)?.id!! == customerId && roles.contains("CUSTOMER")
        }
        return true
    }
}


