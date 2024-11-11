package com.example.ecommerce.product.service

import com.example.ecommerce.product.dto.ProductDTO
import com.example.ecommerce.product.dto.ProductEventDTO
import com.example.ecommerce.product.exception.ConflictException
import com.example.ecommerce.product.exception.NotFoundException
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.repository.CategoryRepository
import com.example.ecommerce.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ProductServiceImpl(@Autowired private var categoryRepository: CategoryRepository,
                         @Autowired @Qualifier("productEventTemplate") private val productEventTemplate: KafkaTemplate<String, ProductEventDTO>,
                         @Autowired private var productRepository: ProductRepository  ) : ProductService
{

    override fun findAll(): List<Product>? {
        return productRepository.findAll()
    }

    @Transactional
    override fun create(product: Product): Product {
        if(productRepository.findByName(product.name) != null)
            throw ConflictException("Product named ${product.name} already exists")
        // Check if category exists
        categoryRepository.findByIdOrNull(product.categoryId)?: throw NotFoundException("Category ${product.categoryId} not found")
        productRepository.save(product)
        val productDTO = ProductDTO(productId = product.productId, name = product.name, description =  product.description ?: "N/A", price = product.price, categoryId = product.categoryId, createdAt =  product.createdAt, updatedAt = product.updatedAt)
        productEventTemplate.send("product_events", ProductEventDTO("CREATE", productDTO))
        return product
    }

    @Transactional
    override fun updateCategory(categoryId: Long, updatedCategory: Category): Category{
        // Checks if name is available
        if(categoryRepository.findByName(updatedCategory.name) != null)
            throw ConflictException("Category with name ${updatedCategory.name} already exists")
        val category = categoryRepository.findByIdOrNull(updatedCategory.id)?: throw NotFoundException("Category with id ${updatedCategory.id} not found")
        category.name = updatedCategory.name
        category.description = updatedCategory.description
        return categoryRepository.save(category)
    }

    @Transactional
    override fun update(productId: Long, updatedProduct: Product): Product {
        // Check if category exists
        categoryRepository.findByIdOrNull(updatedProduct.categoryId)?: throw NotFoundException("Category ${updatedProduct.categoryId} not found")
        val product = productRepository.findByIdOrNull(productId)?: throw NotFoundException("Product (ID: ${productId}) not found")
        val updatedNameCheck = productRepository.findByName(updatedProduct.name)
        if(updatedNameCheck != null)
            if(product.name != updatedNameCheck.name)
                throw ConflictException("Product name ${updatedNameCheck.name} already used for another product")
        product.name = updatedProduct.name
        product.description = if(updatedProduct.description != null) updatedProduct.description else null
        product.price = updatedProduct.price
        product.categoryId = updatedProduct.categoryId
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }

    @Transactional
    override fun delete(productId: Long) {
        val product = productRepository.findByIdOrNull(productId)?: throw NotFoundException("Product (ID: $productId) not found")
        val productDTO = ProductDTO(productId = product.productId, name = product.name, description =  product.description ?: "N/A", price = product.price, categoryId = product.categoryId, createdAt =  product.createdAt, updatedAt = product.updatedAt)
        productEventTemplate.send("product_events", ProductEventDTO("DELETE", productDTO))
        productRepository.delete(product)
    }

    override fun find(productId: Long): Product {
        val product = productRepository.findByIdOrNull(productId)?: throw NotFoundException("Product (ID: $productId) not found")
        return product
    }

    override fun findCategory(categoryId: Long): Category {
        val category = categoryRepository.findByIdOrNull(categoryId)?: throw NotFoundException("Category (ID: $categoryId) not found")
        return category
    }

    override fun findCategories(): List<Category>? {
        return categoryRepository.findAll()
    }

    override fun createCategory(category: Category): Category {
        // Check if category exists
        if(categoryRepository.findByName(category.name) != null)
            throw ConflictException("Category ${category.name} already exists.")
        return categoryRepository.save(category)
    }

    override fun findByName(name: String): Product? {
        val product = productRepository.findByName(name) ?: throw NotFoundException("Product $name not found")
        return product
    }

    @Transactional
    override fun deleteCategory(categoryId: Long) {
        val category = categoryRepository.findByIdOrNull(categoryId)?: throw NotFoundException("Category (ID: $categoryId) not found")
        productRepository.findAll().forEach {
            product -> if(product.categoryId == category.id){
                product.categoryId = -1
                productRepository.save(product)
            }
        }
        categoryRepository.delete(category)
    }
}