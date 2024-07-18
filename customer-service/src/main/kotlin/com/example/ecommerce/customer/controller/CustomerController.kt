package com.example.ecommerce.customer.controller

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.service.CustomerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/customers")
class CustomerController {

    @Autowired
    private lateinit var customerService: CustomerService

    // Endpoint to register new customer
    @PostMapping("/register")
    @Operation(summary = "Register new customer")
    @ApiResponse(responseCode = "200", description = "Successful customer registration")
    fun registerUser(@RequestBody customerDto: CustomerDto): ResponseEntity<Customer> {
        val customer = customerService.registerCustomer(customerDto)
        return ResponseEntity(customer, HttpStatus.CREATED)
    }

    // Endpoint to find customer by id
    @GetMapping("/{customerId}")
    @Operation(summary = "Find customer by ID")
    @ApiResponse(responseCode = "200", description = "Successful customer retrieval")
    fun getUserById(@PathVariable customerId: Long): ResponseEntity<Customer> {
        val customer = customerService.getCustomerById(customerId)
        return ResponseEntity(customer, HttpStatus.OK)
    }

    // Endpoint to update customer
    @PutMapping("/{customerId}")
    @Operation(summary = "Update customer")
    @ApiResponse(responseCode = "200", description = "Successful customer update")
    fun updateUser(
        @PathVariable customerId: Long,
        @RequestBody request: CustomerDto
    ): ResponseEntity<Customer> {
        val customer = customerService.updateCustomer(request)
        return ResponseEntity(customer, HttpStatus.OK)
    }

    // Endpoint to delete user
    @Operation(summary = "Delete customer")
    @ApiResponse(responseCode = "200", description = "Successful customer deletion")
    @DeleteMapping("/{customerId}")
    fun deleteUser(@PathVariable customerId: Long): ResponseEntity<Void> {
        customerService.deleteCustomer(customerId)
        return ResponseEntity(HttpStatus.OK)
    }
}