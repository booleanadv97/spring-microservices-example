package com.example.ecommerce.product.controller

import com.example.ecommerce.product.dto.CategoryDto
import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
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
    @Operation(summary = "Register a new product")
    @ApiResponse(responseCode = "200", description = "Successful product registration")
    @PostMapping("/register")
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<Product> {
        val newProduct = productService.createProduct(productDto)
        return ResponseEntity(newProduct, HttpStatus.CREATED)
    }

    // Endpoint to update an existing product
    @Operation(summary = "Update product")
    @ApiResponse(responseCode = "200", description = "Successful product update")
    @PutMapping("/{id}")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody productDto: ProductDto
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(id, productDto)
        return ResponseEntity(updatedProduct, HttpStatus.OK)
    }

    // Endpoint to delete a product by id
    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "200", description = "Successful product deletion")
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity(HttpStatus.OK)
    }

    // Endpoint to get a product by id
    @Operation(summary = "Find product")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return ResponseEntity(product, HttpStatus.OK)
    }

    // Endpoint to get all product categories
    @Operation(summary = "Get product categories")
    @ApiResponse(responseCode = "200", description = "Successful categories retrieval")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<Category>> {
        val categories = productService.getCategories()
        return ResponseEntity(categories, HttpStatus.OK)
    }

    // Endpoint to create a new product
    @Operation(summary = "Register a new category")
    @ApiResponse(responseCode = "200", description = "Successful category creation")
    @PostMapping("/registerCategory")
    fun createCategory(@RequestBody categoryDto: CategoryDto): ResponseEntity<Category> {
        val newCategory = productService.createCategory(categoryDto)
        return ResponseEntity(newCategory, HttpStatus.CREATED)
    }

    // Endpoint to get a product by name (example of a custom query)
    @Operation(summary = "Get product by name")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @GetMapping("/byname/{name}")
    fun getProductByName(@PathVariable name: String): ResponseEntity<Product?> {
        val product = productService.getProductByName(name)
        return ResponseEntity(product, HttpStatus.OK)
    }

    // Additional endpoints for listing products, searching, etc. can be added here
}