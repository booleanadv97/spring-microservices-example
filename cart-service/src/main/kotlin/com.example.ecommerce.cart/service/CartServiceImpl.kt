package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.client.CustomerClient
import com.example.ecommerce.cart.client.InventoryClient
import com.example.ecommerce.cart.client.ProductClient
import com.example.ecommerce.cart.model.Cart
import com.example.ecommerce.cart.model.CartItem
import com.example.ecommerce.cart.repository.CartRepository
import com.example.ecommerce.common.dto.cart.CartEvent
import com.example.ecommerce.common.dto.customer.ShippingAddressDTO
import com.example.ecommerce.common.dto.product.ProductDTO
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service
class CartServiceImpl(@Autowired private val cartRepository: CartRepository,
                      @Qualifier("cartEventTemplate") private val cartEventTemplate: KafkaTemplate<String, CartEvent>,
                      private val inventoryClient: InventoryClient,
                      private val customerClient: CustomerClient,
                      private val productClient: ProductClient) : CartService {

    // Create a new cart for the user
    @Transactional
    override fun createCart(userId: Long): Cart {
        // Check if the user already has an existing cart
        val existingCart = cartRepository.findByUserId(userId)
        if (existingCart != null) {
            throw RuntimeException("Cart already exists for user $userId")
        }
        val newCart = Cart(userId = userId)
        return cartRepository.save(newCart)
    }

    // Add Item to Cart
    @Transactional
    override fun addItemToCart(userId: Long, productId: Long, quantity: Int): Cart {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        val product = productClient.getProductById(productId)
        val isAvailable = inventoryClient.checkAvailability(productId, quantity);
        if (isAvailable == true) {
            val existingItem = cart.items.find { it.product == product }
            if (existingItem != null) {
                val newQuantity = existingItem.quantity + quantity
                if(newQuantity >= 0)
                    existingItem.quantity += quantity
                else
                    throw RuntimeException("Couldn't update quantity for the product $productId. Quantity must be greater than 0 (supposed quantity: $newQuantity)")
            } else {
                if(quantity > 0)
                    cart.items.add(CartItem(product = product, quantity = quantity))
                else
                    throw RuntimeException("Couldn't update quantity for the product $productId. Quantity must be greater than 0")
            }
            return cartRepository.save(cart)
        }else{
            throw RuntimeException("Product $productId not available")
        }
    }

    // Add Item to Cart
    @Transactional
    override fun removeItemFromCart(userId: Long, productId: Long): Cart {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        val product = productClient.getProductById(productId)
        val existingItem = cart.items.find { it.product == product }
        if (existingItem != null) {
            cart.items.remove(existingItem)
        } else {
            throw RuntimeException("Item ${product.productId} not found for cart ${cart.id}")
        }
        return cartRepository.save(cart)
    }

    // Get Cart
    override fun getCartByUserId(userId: Long): Cart? = cartRepository.findByUserId(userId)

    // Clear Cart
    @Transactional
    override fun clearCart(userId: Long): String {
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        cart.items.clear()
        cartRepository.save(cart)
        return "Cart cleared for user $userId"
    }

    // Checkout Cart
    @Transactional
    override fun checkoutCart(userId: Long, shippingAddressId: Long): String {
        // Fetch cart and customer data
        val cart = cartRepository.findByUserId(userId) ?: throw RuntimeException("Cart not found for user $userId")
        val customerDTO = customerClient.getCustomerDTO(userId)

        // Initialize customerShippingAddress as null
        var customerShippingAddress: ShippingAddressDTO? = null

        // Iterate through the customer's shipping addresses
        for (shippingAddress in customerDTO.shippingAddresses) {
            if (shippingAddress.shippingAddressId == shippingAddressId) {
                customerShippingAddress = shippingAddress
                break  // Exit loop once the matching address is found
            }
        }
        println(customerShippingAddress.toString())
        println(customerShippingAddress.toString())
        // Check if customerShippingAddress is still null (not found)
        if (customerShippingAddress == null) {
            throw RuntimeException("Shipping address not found for user $userId")
        }

        // Prepare cart items
        val cartItemsDTO = mutableListOf<com.example.ecommerce.common.dto.cart.CartItem>()
        cart.items.forEach {
            cartItemsDTO.add(com.example.ecommerce.common.dto.cart.CartItem(it.cartItemId, it.product, it.quantity))
        }

        // Send cart event
        cartEventTemplate.send("cart_events", CartEvent(
            eventType = "CHECKOUT",
            userId = userId,
            cartItems = cartItemsDTO,
            shippingAddress = customerShippingAddress  // This is safe since we checked it's not null
        ))

        // Delete the cart and return the result
        cartRepository.delete(cart)
        return "Checkout completed for user $userId"
    }

    override fun checkCartAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        if(!roles.contains("CUSTOMER_MANAGER")){
            val customerDTO = customerClient.getCustomerDTO(customerId)
            return customerDTO.username == username && roles.contains("CUSTOMER")
        }
        return true
    }
}