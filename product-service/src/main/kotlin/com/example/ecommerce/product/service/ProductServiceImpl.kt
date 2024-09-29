package com.example.ecommerce.product.service

import com.example.ecommerce.common.dto.product.ProductEventDTO
import com.example.ecommerce.common.dto.product.CategoryDTO
import com.example.ecommerce.common.dto.product.ProductDTO
import com.example.ecommerce.product.client.KeycloakClient
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.repository.CategoryRepository
import com.example.ecommerce.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl(@Autowired private var categoryRepository: CategoryRepository,
                         private val keycloakClient: KeycloakClient,
                         @Autowired @Qualifier("productEventTemplate") private val productEventTemplate: KafkaTemplate<String, ProductEventDTO>,
                         @Autowired private var productRepository: ProductRepository  ) : ProductService {
    override fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    override fun createProduct(productDto: ProductDTO): Product {
        // Check if category exists
        categoryRepository.findByIdOrNull(productDto.categoryId)?: throw RuntimeException("Category ${productDto.categoryId} not found")
        val newProduct = Product(
            name = productDto.name,
            description = productDto.description,
            price = productDto.price,
            categoryId = productDto.categoryId
        )
        val savedProduct = productRepository.save(newProduct)
        val savedProductToDto = ProductDTO(savedProduct.productId, savedProduct.name, savedProduct.description!!, savedProduct.price, savedProduct.categoryId, savedProduct.createdAt, savedProduct.updatedAt)
        productEventTemplate.send("product_events", ProductEventDTO("CREATE", savedProductToDto))
        return savedProduct
    }

    override fun updateProduct(productDto: ProductDTO): Product {
        // Check if category exists
        categoryRepository.findByIdOrNull(productDto.categoryId)?: throw RuntimeException("Category ${productDto.categoryId} not found")
        val product = productRepository.findByIdOrNull(productDto.productId)?: throw RuntimeException("Product (ID: ${productDto.productId}) not found")
        product.name = productDto.name
        product.description = productDto.description
        product.price = productDto.price
        product.categoryId = productDto.categoryId
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }

    override fun deleteProduct(productId: Long) {
        val product = productRepository.findByIdOrNull(productId)?: throw RuntimeException("Product (ID: $productId) not found")
        productRepository.delete(product)
    }

    override fun getProductById(productId: Long): ProductDTO {
        val product = productRepository.findByIdOrNull(productId)?: throw RuntimeException("Product (ID: $productId) not found")
        val productDto = ProductDTO(productId = product.productId, name = product.name, description = product.description!!, price =  product.price, categoryId = product.categoryId)
        return productDto
    }

    override fun getCategories(): List<CategoryDTO> {
        val categories = mutableListOf<CategoryDTO>()
        categoryRepository.findAll().forEach {
            categories.add(CategoryDTO(it.name, it.description))
        }
        return categories
    }

    override fun createCategory(categoryDto: CategoryDTO): Category {
        // Check if category exists
        if(categoryRepository.findByName(categoryDto.name) != null)
            throw RuntimeException("Category ${categoryDto.name} already exists.")
        val category = Category(name = categoryDto.name, description = categoryDto.description)
        return categoryRepository.save(category)

    }

    override fun getProductByName(name: String): ProductDTO? {
        val product = productRepository.findByName(name) ?: throw RuntimeException("Product $name not found")
        val productDto = ProductDTO(name = product.name, description = product.description!!, price = product.price, categoryId = product.categoryId)
        return productDto
    }

    override fun login(username: String, password: String): String? {
        val jwt = keycloakClient.loginProduct(username, password)
        return jwt
    }
}