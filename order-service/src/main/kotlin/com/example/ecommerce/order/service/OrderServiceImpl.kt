package com.example.ecommerce.order.service

import com.example.ecommerce.common.dto.cart.CartEvent
import com.example.ecommerce.common.dto.order.*
import com.example.ecommerce.common.dto.product.ProductEventDTO
import com.example.ecommerce.order.client.CustomerClient
import com.example.ecommerce.order.client.KeycloakClient
import com.example.ecommerce.order.model.Order
import com.example.ecommerce.order.model.OrderItem
import com.example.ecommerce.order.repository.OrderRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull


@Service
class InventoryServiceImpl(@Autowired private var orderRepository: OrderRepository,
                           private val keycloakClient: KeycloakClient,
                           private val customerClient: CustomerClient,
                           @Autowired @Qualifier("orderEventTemplate") private val orderEventTemplate: KafkaTemplate<String, OrderEvent>,
): OrderService {
    @Transactional
    override fun createOrder(cartEvent: CartEvent): Order? {
        val orderItems = mutableListOf<OrderItem>()
        val orderDTOItems = mutableListOf<OrderItemDTO>()
        cartEvent.cartItems.forEach{
            cartItem -> orderItems.add(OrderItem(cartItem = cartItem))
            orderDTOItems.add(OrderItemDTO(cartItem = cartItem))
        }
        val order = Order(userId = cartEvent.userId, items = orderItems, shippingAddress = cartEvent.shippingAddress!!, status = OrderStatusEnum.PENDING, shippingStatus = ShipmentStatusEnum.DISPATCHED)
        orderEventTemplate.send("order_events",
            OrderEvent("CREATE",
                OrderDTO(id = order.id,
                    userId = order.userId,
                    items = orderDTOItems,
                    shippingAddress = order.shippingAddress,
                    status = order.status,
                    shippingStatus =  order.shippingStatus,
                    createdAt = order.createdAt
                )
            )
        )
        return orderRepository.save(order)
    }

    override fun findOrderById(orderId: Long): Order? {
        val order = orderRepository.findById(orderId).getOrNull() ?: throw RuntimeException("Order not found")
        return order
    }

    override fun findOrderByUserId(userId: Long): Order? {
        val order = orderRepository.findByUserId(userId)?: throw RuntimeException("Order not found")
        return order
    }

    override fun updateOrderStatus(orderId: Long, status: OrderStatusEnum): Order? {
        val order = orderRepository.findById(orderId).getOrNull() ?: throw RuntimeException("Order not found")
        order.status = status
        orderRepository.save(order)
        return order
    }

    override fun deleteOrder(orderId: Long) {
        val order = orderRepository.findById(orderId).getOrNull() ?: throw RuntimeException("Order not found")
        orderRepository.delete(order)
    }

    override fun checkOrderAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        if(!roles.contains("ORDER_MANAGER")){
            val customerDTO = customerClient.getCustomerDTO(customerId)
            return customerDTO.username == username && roles.contains("CUSTOMER")
        }
        return true
    }

    override fun login(username: String, password: String): String? {
        val jwt = keycloakClient.loginOrder(username, password)
        return jwt
    }
}
