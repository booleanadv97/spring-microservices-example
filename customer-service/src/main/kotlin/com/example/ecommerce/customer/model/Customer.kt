package com.example.ecommerce.customer.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Customer(
    var username: String? = null,
    var email: String? = null,
    var password: String? = null,
    var updatedAt: LocalDateTime? = null,
    val createdAt: LocalDateTime? = LocalDateTime.now(),
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null)

