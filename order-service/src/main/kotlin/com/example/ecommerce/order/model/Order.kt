package com.example.ecommerce.order.model

import com.example.ecommerce.order.dto.cart.CartItem
import com.example.ecommerce.order.dto.customer.ShippingAddressDTO
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(nullable = false)
    val customerId: Long,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER, orphanRemoval = true)
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

enum class OrderStatusEnum {
    PENDING,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    DECLINED,
    REFUNDED,
    CANCELLED
}

enum class ShipmentStatusEnum {
    DISPATCHED,
    IN_TRANSIT,
    OUT_FOR_DELIVERY,
    DELIVERED,
    DELAYED,
    LOST,
}


