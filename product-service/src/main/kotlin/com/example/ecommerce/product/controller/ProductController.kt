package com.example.ecommerce.product.controller

import com.example.ecommerce.product.dto.CategoryDto
import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products/api")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    // Endpoint to create a new product
    @Operation(summary = "Register a new product")
    @ApiResponse(responseCode = "200", description = "Successful product registration")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @PostMapping("/register")
    fun createProduct(@RequestBody productDto: ProductDto): ResponseEntity<Product> {
        val newProduct = productService.createProduct(productDto)
        return ResponseEntity.ok(newProduct)
    }

    // Endpoint to update an existing product
    @Operation(summary = "Update product")
    @ApiResponse(responseCode = "200", description = "Successful product update")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody productDto: ProductDto
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(id, productDto)
        return ResponseEntity.ok(updatedProduct)
    }

    // Endpoint to delete a product by id
    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "200", description = "Successful product deletion")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @DeleteMapping("/{id}")
    fun deleteProduct(@PathVariable id: Long): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.ok(null)
    }

    // Endpoint to get a product by id
    @Operation(summary = "Find product")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/{id}")
    fun getProductById(@PathVariable id: Long): ResponseEntity<Product> {
        val product = productService.getProductById(id)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all product categories
    @Operation(summary = "Get product categories")
    @ApiResponse(responseCode = "200", description = "Successful categories retrieval")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<Category>> {
        val categories = productService.getCategories()
        return ResponseEntity.ok(categories)
    }

    // Endpoint to create a new product
    @Operation(summary = "Register a new category")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful category creation")
    @PostMapping("/registerCategory")
    fun createCategory(@RequestBody categoryDto: CategoryDto): ResponseEntity<Category> {
        val newCategory = productService.createCategory(categoryDto)
        return ResponseEntity.ok(newCategory)
    }

    // Endpoint to get a product by name (example of a custom query)
    @Operation(summary = "Get product by name")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @GetMapping("/byname/{name}")
    fun getProductByName(@PathVariable name: String): ResponseEntity<Product?> {
        val product = productService.getProductByName(name)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all products
    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of products")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/")
    fun getAllStocks(authentication: JwtAuthenticationToken): ResponseEntity<List<Product>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }
}