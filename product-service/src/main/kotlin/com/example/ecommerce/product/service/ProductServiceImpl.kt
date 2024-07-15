package com.example.ecommerce.product.service

import com.example.ecommerce.product.dto.ProductDto
import com.example.ecommerce.product.exception.InvalidParameterException
import com.example.ecommerce.product.model.Category
import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.repository.CategoryRepository
import com.example.ecommerce.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ProductServiceImpl : ProductService {

    override fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    @Autowired
    private lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    override fun createProduct(productDto: ProductDto): Product {
        val newProduct = Product(
            name = productDto.name,
            description = productDto.description,
            price = productDto.price,
            category = productDto.category
        )
        return productRepository.save(newProduct)
    }

    override fun updateProduct(id: Long, productDto: ProductDto): Product {
        val product = productRepository.findById(id)
            .orElseThrow { InvalidParameterException("Product $id not found")
            }
        product.name = productDto.name
        product.description = productDto.description
        product.price = productDto.price
        product.category = productDto.category
        product.updatedAt = LocalDateTime.now()
        return productRepository.save(product)
    }

    override fun deleteProduct(id: Long) {
        val product = productRepository.findById(id)
            .orElseThrow { InvalidParameterException("Product $id not found")
            }
        productRepository.delete(product)
    }

    override fun getProductById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { InvalidParameterException("Product $id not found")
            }
    }

    override fun getCategories(): List<Category> {
        return categoryRepository.findAllBy()
    }

    override fun getProductByName(name: String): Product? {
        return productRepository.findByName(name)?: throw InvalidParameterException("Product $name not found")
    }
}