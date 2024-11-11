package com.example.ecommerce.product.model

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.persistence.*

@Entity
data class Category(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Category id", example = "1", nullable = true)
    val id: Long = 0,
    @Column(unique = true, nullable = false)
    @Schema(description = "Category name", example = "Electronics", nullable = false)
    var name: String,
    @Column(nullable = true)
    @Schema(description = "Category description", example = "Explore our wide range of electronic devices and accessories, including smartphones, laptops, televisions, and home entertainment systems. Stay connected, entertained, and productive with the latest technology from top brands.", nullable = true)
    var description : String
)