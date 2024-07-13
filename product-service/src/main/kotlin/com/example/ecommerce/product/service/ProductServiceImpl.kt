package com.example.ecommerce.product.service

import com.example.ecommerce.product.model.Product
import com.example.ecommerce.product.repository.ProductRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ProductServiceImpl : ProductService {

    @Autowired
    @Qualifier("productRepository")
    private lateinit var productRepository: ProductRepository

    override fun createProduct(name: String, description: String, price: Double, quantity: Int): Product {
        val newProduct = Product(
            name = name,
            description = description,
            price = price,
            quantity = quantity
        )
        return productRepository.save(newProduct)
    }

    override fun updateProduct(id: Long, name: String, description: String, price: Double, quantity: Int): Product {
        val product = productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Product with id $id not found") }

        product.name = name
        product.description = description
        product.price = price
        product.quantity = quantity

        return productRepository.save(product)
    }

    override fun deleteProduct(id: Long) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Product with id $id not found")
        }
    }

    override fun getProductById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { NoSuchElementException("Product with id $id not found") }
    }

    override fun getProductByName(name: String): Product? {
        return productRepository.findByName(name)
    }
}