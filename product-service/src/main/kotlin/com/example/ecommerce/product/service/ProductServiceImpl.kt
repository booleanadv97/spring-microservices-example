package com.example.ecommerce.product.service

import com.example.ecommerce.product.dto.CategoryDto
import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.repository.CategoryRepository
import com.example.ecommerce.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(@Autowired private var categoryRepository: CategoryRepository,
                         @Autowired private val kafkaTemplate: KafkaTemplate<String, Product>,
                         @Autowired private var productRepository: ProductRepository  ) : ProductService {
    override fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    override fun createProduct(productDto: ProductDto): Product {
        // Check if category exists
        categoryRepository.findByIdOrNull(productDto.categoryId)?: throw RuntimeException("Category ${productDto.categoryId} not found")
        val newProduct = Product(
            name = productDto.name,
            description = productDto.description,
            price = productDto.price,
            categoryId = productDto.categoryId
        )
        val savedProduct = productRepository.save(newProduct)
        kafkaTemplate.send("create_default_stock_for_product", savedProduct)
        return savedProduct
    }

    override fun updateProduct(id: Long, productDto: ProductDto): Product {
        // Check if category exists
        categoryRepository.findByIdOrNull(productDto.categoryId)?: throw RuntimeException("Category ${productDto.categoryId} not found")
        val product = productRepository.findByIdOrNull(id)?: throw RuntimeException("Product (ID: $id) not found")
        product.name = productDto.name
        product.description = productDto.description
        product.price = productDto.price
        product.categoryId = productDto.categoryId
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }

    override fun deleteProduct(id: Long) {
        val product = productRepository.findByIdOrNull(id)?: throw RuntimeException("Product (ID: $id) not found")
        productRepository.delete(product)
    }

    override fun getProductById(id: Long): Product {
        return productRepository.findByIdOrNull(id) ?: throw RuntimeException("Product (ID: $id) not found")
    }

    override fun getCategories(): List<Category> {
        return categoryRepository.findAllBy()
    }

    override fun createCategory(categoryDto: CategoryDto): Category {
        // Check if category exists
        if(categoryRepository.findByName(categoryDto.name) != null)
            throw RuntimeException("Category ${categoryDto.name} already exists.")
        val category = Category(name = categoryDto.name, description = categoryDto.description)
        return categoryRepository.save(category)

    }

    override fun getProductByName(name: String): Product? {
        val product = productRepository.findByName(name) ?: throw RuntimeException("Product $name not found")
        return product
    }
}