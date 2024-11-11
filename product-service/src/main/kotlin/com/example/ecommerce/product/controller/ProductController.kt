package com.example.ecommerce.product.controller

import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products/api/v1")
class ProductController {

    @Autowired
    private lateinit var productService: ProductService

    // Endpoint to create a new product
    @Operation(summary = "Create a new product")
    @ApiResponse(responseCode = "200", description = "Successful product creation")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Category with given id not found")
    @ApiResponse(responseCode = "409", description = "Given name already used for another product")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @PostMapping("/create")
    fun create(@RequestBody product: Product): ResponseEntity<Product> {
        val newProduct = productService.create(product)
        return ResponseEntity.ok(newProduct)
    }

    // Endpoint to update an existing product
    @Operation(summary = "Update product")
    @ApiResponse(responseCode = "200", description = "Successful product update")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "409", description = "Given name already used for another product")
    @ApiResponse(responseCode = "404", description = "Product with given id not found")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    fun update(
        @PathVariable productId: Long,
        @RequestBody updatedProduct: Product
    ): ResponseEntity<Product> {
        val product = productService.update(productId, updatedProduct)
        return ResponseEntity.ok(product)
    }

    // Endpoint to delete a product 
    @Operation(summary = "Delete product")
    @ApiResponse(responseCode = "200", description = "Successful product removal")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Product with given id not found")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @DeleteMapping("/{productId}")
    fun deleteProduct(@PathVariable productId: Long): ResponseEntity<Void> {
        productService.delete(productId)
        return ResponseEntity.ok(null)
    }

    // Endpoint to find a product
    @Operation(summary = "Find product")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Product with given id not found")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/{productId}")
    fun find(@PathVariable productId: Long): ResponseEntity<Product> {
        val product = productService.find(productId)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all product categories
    @Operation(summary = "Get product categories")
    @ApiResponse(responseCode = "200", description = "Successful categories retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/categories")
    fun getCategories(): ResponseEntity<List<Category>> {
        val categories = productService.findCategories()
        return ResponseEntity.ok(categories)
    }

    // Endpoint to create a new category
    @Operation(summary = "Register a new category")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @ApiResponse(responseCode = "200", description = "Successful category creation")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "409", description = "Category with given name already exists")
    @PostMapping("/registerCategory")
    fun createCategory(@RequestBody newCategory: Category): ResponseEntity<Category> {
        val category = productService.createCategory(newCategory)
        return ResponseEntity.ok(category)
    }

    // Endpoint to update an existing category
    @Operation(summary = "Update category")
    @ApiResponse(responseCode = "200", description = "Successful category update")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "409", description = "Given name already used for another category")
    @ApiResponse(responseCode = "404", description = "Category with given id not found")
    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    fun updateCategory(
        @PathVariable categoryId: Long,
        @RequestBody updatedCategory: Category
    ): ResponseEntity<Category> {
        val category = productService.updateCategory(categoryId, updatedCategory)
        return ResponseEntity.ok(category)
    }

    // Endpoint to delete a category
    @Operation(summary = "Delete category")
    @ApiResponse(responseCode = "200", description = "Successful category removal")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Category with given id not found")
    @PreAuthorize("hasRole('PRODUCT_MANAGER')")
    @DeleteMapping("/categories/{categoryId}")
    fun deleteCategory(@PathVariable categoryId: Long): ResponseEntity<Void> {
        productService.deleteCategory(categoryId)
        return ResponseEntity.ok(null)
    }

    // Endpoint to find a category
    @Operation(summary = "Find category")
    @ApiResponse(responseCode = "200", description = "Successful category retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Category with given id not found")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("categories/{categoryId}")
    fun findCategory(@PathVariable categoryId: Long): ResponseEntity<Category> {
        val category = productService.findCategory(categoryId)
        return ResponseEntity.ok(category)
    }

    // Endpoint to get a product by name
    @Operation(summary = "Find product by name")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @ApiResponse(responseCode = "200", description = "Successful product retrieval")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @ApiResponse(responseCode = "404", description = "Product with given name not found")
    @GetMapping("/byName/{name}")
    fun getProductByName(@PathVariable name: String): ResponseEntity<Product?> {
        val product = productService.findByName(name)
        return ResponseEntity.ok(product)
    }

    // Endpoint to get all products
    @Operation(summary = "Find all products")
    @ApiResponse(responseCode = "200", description = "Successful retrieval of all products")
    @ApiResponse(responseCode = "401", description = "Not authenticated or authorized to perform this operation")
    @PreAuthorize("hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    @GetMapping("/")
    fun findAll(): ResponseEntity<List<Product>> {
        val products = productService.findAll()
        return ResponseEntity.ok(products)
    }
}