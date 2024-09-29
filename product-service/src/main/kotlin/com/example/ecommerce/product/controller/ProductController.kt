package com.example.ecommerce.product.controller

import com.example.ecommerce.common.dto.product.CategoryDTO
import com.example.ecommerce.common.dto.product.ProductDTO
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

    // Endpoint to login
    @PostMapping("/login")
    @Operation(summary = "Login as product manager")
    @ApiResponse(responseCode = "200", description = "Successful login")
    fun login(@RequestParam username : String, @RequestParam password: String): ResponseEntity<String> {
        val jwt = productService.login(username, password)
        return ResponseEntity.ok(jwt)
    }

    // Endpoint to create a new product
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "200", description = "Successful product creation")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @PostMapping("/create")
    fun createProduct(@RequestBody productDto: ProductDTO): ResponseEntity<Product> {
        val newProduct = productService.createProduct(productDto)
        return ResponseEntity.ok(newProduct)
    }

    // Endpoint to update an existing product
    @Operation(summary = "Update product")
    @ApiResponse(responseCode = "200", description = "Successful product update")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    fun updateProduct(
        @RequestBody productDto: ProductDTO
    ): ResponseEntity<Product> {
        val updatedProduct = productService.updateProduct(productDto)
        return ResponseEntity.ok(updatedProduct)
    }

    // Endpoint to delete a product 
    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "200", description = "Successful product removal")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long): ResponseEntity<Void> {
        productService.deleteProduct(productId)
        return ResponseEntity.ok(null)
    }

    // Endpoint to find a product
    @Operation(summary = "Find product")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ResponseEntity<ProductDTO> {
        val product = productService.getProductById(productId)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all product categories
    @Operation(summary = "Get product categories")
    @ApiResponse(responseCode = "200", description = "Successful categories retrieval")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<CategoryDTO>> {
        val categories = productService.getCategories()
        return ResponseEntity.ok(categories)
    }

    // Endpoint to create a new category
    @Operation(summary = "Register a new category")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful category creation")
    @PostMapping("/registerCategory")
    fun createCategory(@RequestBody categoryDto: CategoryDTO): ResponseEntity<Category> {
        val newCategory = productService.createCategory(categoryDto)
        return ResponseEntity.ok(newCategory)
    }

    // Endpoint to get a product by name
    @Operation(summary = "Get product by name")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @GetMapping("/byName/{name}")
    fun getProductByName(@PathVariable name: String): ResponseEntity<ProductDTO?> {
        val product = productService.getProductByName(name)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all products
    @Operation(summary = "Get all products")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all products")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/")
    fun getAllProducts(authentication: JwtAuthenticationToken): ResponseEntity<List<Product>> {
        val products = productService.getAllProducts()
        return ResponseEntity.ok(products)
    }
}