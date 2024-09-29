package com.example.ecommerce.customer.controller

import com.example.ecommerce.common.dto.customer.CustomerDTO
import com.example.ecommerce.customer.model.Customer
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
    private lateinit var customerService: CustomerService

    // Endpoint to create a new customer
    @PostMapping("/create")
    @Operation(summary = "Create a new customer")
    @ApiResponse(responseCode = "200", description = "Successful customer creation")
    fun registerCustomer(@RequestBody customerRegistration: Customer): ResponseEntity<Customer> {
        val newCustomer = customerService.registerCustomer(customerRegistration)
        return ResponseEntity.ok(newCustomer)
    }

    // Endpoint to customer login
    @PostMapping("/login_customer")
    @Operation(summary = "Customer login")
    @ApiResponse(responseCode = "200", description = "Successful customer login")
    fun loginCustomer(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = customerService.loginCustomer(username, password)
        return ResponseEntity.ok(jwt)
    }

    // Endpoint to find customer by id
    @GetMapping("/{customerId}")
    @Operation(summary = "Find customer by ID")
    @ApiResponse(responseCode = "200", description = "Successful customer retrieval")
    fun getCustomerById(authentication: JwtAuthenticationToken,
                    @PathVariable customerId: Long): ResponseEntity<Customer> {
        val customer = customerService.getCustomerById(customerId)
        if(customerService.checkCustomerAuth(authentication,customerId)) {
            return ResponseEntity.ok(customer)
        }else{
            throw RuntimeException("You are not authorized to perform this operation")
        }
    }

    @GetMapping("/{customerId}/DTO")
    @Operation(summary = "Get customer DTO")
    @ApiResponse(responseCode = "200", description = "Success")
    fun getCustomerDTO(authentication: JwtAuthenticationToken,
                    @PathVariable customerId: Long): ResponseEntity<CustomerDTO> {
        if(customerService.checkCustomerAuth(authentication,customerId)) {
            val customerDTO = customerService.getCustomerDTO(customerId)
            return ResponseEntity.ok(customerDTO)
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
        @RequestBody updatedCustomer: Customer,
    ): ResponseEntity<Customer> {
        if(customerService.checkCustomerAuth(authentication,customerId)) {
            val customer = customerService.updateCustomer(updatedCustomer)
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
        if(customerService.checkCustomerAuth(authentication,customerId)) {
            customerService.deleteCustomer(customerId)
            return ResponseEntity.ok(null)
        }else{
            throw RuntimeException("You are not authorized to perform this operation")
        }
    }
}


