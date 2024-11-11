package com.example.ecommerce.customer.controller

import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customers/api/v1")
class CustomerController {

    @Autowired
    private lateinit var customerService: CustomerService

    // Endpoint to create a new customer
    @PostMapping("/create")
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "200", description = "Successful customer creation")
    @ApiResponse(responseCode = "409", description = "Customer with given username already exists")
    fun create(@RequestBody customerRegistration: Customer): ResponseEntity<Customer> {
        val newCustomer = customerService.create(customerRegistration)
        return ResponseEntity.ok(newCustomer)
    }

    // Endpoint to find customer by id
    @GetMapping("/{customerId}")
    @Operation(summary = "Find customer by customer id")
    @ApiResponse(responseCode = "200", description = "Successful customer retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Customer with given id doesn't exists")
    fun findCustomer(authentication: JwtAuthenticationToken,
                    @PathVariable customerId: Long): ResponseEntity<Customer> {
        val customer = customerService.findCustomer(authentication, customerId)
        return ResponseEntity.ok(customer)
    }

    // Endpoint to update customer
    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Successful customer update")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Customer with given id doesn't exists")
    @ApiResponse(responseCode = "409", description = "Username already used")
    fun update(authentication: JwtAuthenticationToken,
        @PathVariable customerId: Long,
        @RequestBody updatedCustomer: Customer,
    ): ResponseEntity<Customer> {
            val customer = customerService.update(authentication, customerId, updatedCustomer)
            return ResponseEntity.ok(customer)
    }

    // Endpoint to delete user
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Successful customer deletion")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @DeleteMapping("/{customerId}")
    fun delete(authentication: JwtAuthenticationToken, @PathVariable customerId: Long): ResponseEntity<Void> {
            customerService.delete(authentication, customerId)
            return ResponseEntity.ok(null)
    }
}



