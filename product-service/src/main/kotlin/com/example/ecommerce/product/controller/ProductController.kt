package com.example.ecommerce.product.controller

import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/products")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    // Endpoint to create a new product
    @PostMapping("/register")
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<Product> {
        val newProduct = productService.createProduct(productDto)
        return ResponseEntity(newProduct, HttpStatus.CREATED)
    }

    // Endpoint to update an existing product
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody productDto: ProductDto
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(id, productDto)
        return ResponseEntity(updatedProduct, HttpStatus.OK)
    }

    // Endpoint to delete a product by id
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    // Endpoint to get a product by id
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return ResponseEntity(product, HttpStatus.OK)
    }

    // Endpoint to get a product by id
    @GetMapping("/categories")
    fun getProductById(): ResponseEntity<List<Category>> {
        val categories = productService.getCategories()
        return ResponseEntity(categories, HttpStatus.OK)
    }

    // Endpoint to get a product by name (example of a custom query)
    @GetMapping("/byname/{name}")
    fun getProductByName(@PathVariable name: String): ResponseEntity<Product?> {
        val product = productService.getProductByName(name)
        return ResponseEntity(product, HttpStatus.OK)
    }

    // Additional endpoints for listing products, searching, etc. can be added here
}