package com.example.ecommerce.order.model

import com.example.ecommerce.common.dto.cart.CartItem
import com.example.ecommerce.common.dto.customer.ShippingAddressDTO
import com.example.ecommerce.common.dto.order.OrderStatusEnum
import com.example.ecommerce.common.dto.order.ShipmentStatusEnum
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val userId: Long,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    val items: MutableList<OrderItem> = mutableListOf(),
    @Embedded @Column(nullable = false)
    val shippingAddress: ShippingAddressDTO,
    @Column(nullable = false)
    var shippingStatus: ShipmentStatusEnum,
    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(nullable = false)
    var status: OrderStatusEnum,
)

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false) @Embedded
    val cartItem: CartItem,
)





