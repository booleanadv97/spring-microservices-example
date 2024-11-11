package com.example.ecommerce.customer.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class Customer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val givenName: String,

    @Column(nullable = false)
    val familyName: String,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false, unique = true)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Embedded
    var address: Address? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    var shippingAddresses: MutableList<ShippingAddress> = mutableListOf(),

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

@Embeddable
data class Address(
    val streetAddress: String,
    val addressLocality: String,
    val addressRegion: String,
    val postalCode: String,
    val addressCountry: String
)

@Entity
data class ShippingAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val shippingAddressId: Long,
    var recipientName: String,
    @Embedded
    var address: Address,
)

