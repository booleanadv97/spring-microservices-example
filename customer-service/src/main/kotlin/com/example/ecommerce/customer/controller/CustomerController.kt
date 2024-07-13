package com.example.ecommerce.customer.controller

import com.example.ecommerce.customer.dto.CustomerDto
import com.example.ecommerce.customer.model.Address
import com.example.ecommerce.customer.model.Customer
import com.example.ecommerce.customer.service.CustomerService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/customers")
class CustomerController {

    @Autowired
    private lateinit var customerService: CustomerService

    @PostMapping("/register")
    fun registerUser(@RequestBody customerDto: CustomerDto): ResponseEntity<Customer> {
        val customer = customerService.registerCustomer(customerDto)
        return ResponseEntity(customer, HttpStatus.CREATED)
    }

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): ResponseEntity<Customer> {
        val user = customerService.getCustomerById(userId)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @PutMapping("/{userId}")
    fun updateUser(
        @PathVariable userId: Long,
        @RequestBody request: CustomerDto
    ): ResponseEntity<Customer> {
        val user = customerService.updateCustomer(request)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @DeleteMapping("/{userId}")
    fun deleteUser(@PathVariable userId: Long): ResponseEntity<Void> {
        customerService.deleteCustomer(userId)
        return ResponseEntity(HttpStatus.OK)
    }
}