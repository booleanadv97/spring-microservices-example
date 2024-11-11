package com.example.ecommerce.cart.service

import com.example.ecommerce.cart.client.CustomerClient
import com.example.ecommerce.cart.client.InventoryClient
import com.example.ecommerce.cart.client.ProductClient
import com.example.ecommerce.cart.dto.CartEvent
import com.example.ecommerce.cart.dto.customer.ShippingAddressDTO
import com.example.ecommerce.cart.exception.ConflictException
import com.example.ecommerce.cart.exception.NotFoundException
import com.example.ecommerce.cart.exception.UnauthorizedException
import com.example.ecommerce.cart.model.Cart
import com.example.ecommerce.cart.model.CartItem
import com.example.ecommerce.cart.repository.CartRepository
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
    override fun create(authentication: JwtAuthenticationToken, customerId: Long): Cart {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        // Check if the user already has an existing cart
        val existingCart = cartRepository.findByCustomerId(customerId)
        if (existingCart != null) {
            throw ConflictException("Cart already exists for user $customerId")
        }
        val newCart = Cart(customerId = customerId)
        return cartRepository.save(newCart)
    }

    // Add Item to Cart
    @Transactional
    override fun addItem(authentication: JwtAuthenticationToken, customerId: Long, productId: Long, quantity: Int): Cart {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        val cart = cartRepository.findByCustomerId(customerId) ?: throw NotFoundException("Cart not found for user $customerId")
        val product = productClient.find(productId)
        val isAvailable = if(quantity < 1) true else inventoryClient.checkAvailability(productId, quantity)
        if (isAvailable == true) {
            val existingItem = cart.items.find { it.product == product }
            if (existingItem != null) {
                val newQuantity = existingItem.quantity + quantity
                if(newQuantity >= 0)
                    existingItem.quantity += quantity
                else
                    throw ConflictException("Couldn't update quantity for the product $productId. Quantity must be greater than 0 (supposed quantity: $newQuantity)")
            } else {
                if(quantity > 0)
                    cart.items.add(CartItem(product = product, quantity = quantity))
                else
                    throw ConflictException("Couldn't add product $productId to the cart. Quantity must be greater than 0")
            }
            return cartRepository.save(cart)
        }else{
            throw ConflictException("Product $productId not available")
        }
    }

    // Add Item to Cart
    @Transactional
    override fun removeItem(authentication: JwtAuthenticationToken, customerId: Long, productId: Long): Cart {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        val cart = cartRepository.findByCustomerId(customerId) ?: throw NotFoundException("Cart not found for user $customerId")
        val product = productClient.find(productId)
        val existingItem = cart.items.find { it.product == product }
        if (existingItem != null) {
            cart.items.remove(existingItem)
        } else {
            throw NotFoundException("Item ${product.productId} not found for cart ${cart.id}")
        }
        return cartRepository.save(cart)
    }

    // Get Cart
    override fun find(authentication: JwtAuthenticationToken, customerId: Long): Cart? {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        val cart = cartRepository.findByCustomerId(customerId) ?: throw NotFoundException("Cart not found for user $customerId")
        return cart
    }

    // Clear Cart
    @Transactional
    override fun clear(authentication: JwtAuthenticationToken, customerId: Long): String {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        val cart = cartRepository.findByCustomerId(customerId) ?: throw NotFoundException("Cart not found for user $customerId")
        cart.items.clear()
        cartRepository.save(cart)
        return "Cart cleared for user $customerId"
    }

    // Checkout Cart
    @Transactional
    override fun checkout(authentication: JwtAuthenticationToken, customerId: Long, shippingAddressId: Long): String {
        if(!checkCartAuth(authentication, customerId)) throw UnauthorizedException("You are not authorized to perform this operation")
        // Fetch cart and customer data
        val cart = cartRepository.findByCustomerId(customerId) ?: throw NotFoundException("Cart not found for user $customerId")
        val customer = customerClient.findCustomer(customerId)

        // Initialize customerShippingAddress as null
        var customerShippingAddress: ShippingAddressDTO? = null

        if(customer.shippingAddresses == null)
            throw NotFoundException("Customer has not provided shipping address")

        // Iterate through the customer's shipping addresses
        for (shippingAddress in customer.shippingAddresses) {
            if (shippingAddress.shippingAddressId == shippingAddressId) {
                customerShippingAddress = shippingAddress
                break  // Exit loop once the matching address is found
            }
        }
        // Check if customerShippingAddress is null (not found)
        if (customerShippingAddress == null) {
            throw NotFoundException("Shipping address not found for customer $customerId")
        }

        // Prepare cart items
        val cartItemsDTO = mutableListOf<com.example.ecommerce.cart.dto.CartItem>()
        cart.items.forEach {
            cartItemsDTO.add(com.example.ecommerce.cart.dto.CartItem(it.cartItemId, it.product, it.quantity))
        }

        // Send cart event
        cartEventTemplate.send("cart_events", CartEvent(
            eventType = "CHECKOUT",
            customerId = customerId,
            cartItems = cartItemsDTO,
            shippingAddress = customerShippingAddress
        ))

        // Delete the cart and return the result
        cartRepository.delete(cart)
        return "Checkout completed for customer $customerId"
    }

    // Checks whether the user is authorized to perform cart operations or not
    fun checkCartAuth(authentication: JwtAuthenticationToken, customerId: Long): Boolean {
        // Retrieve username and roles from the JWT
        val username = authentication.token.getClaimAsString("preferred_username")
        val roles = (authentication.token.claims["realm_access"] as Map<*, *>)["roles"] as List<*>
        // If the user doesn't have the customer manager role
        // proceeds checking whether the customer who owns the cart is the authenticated user or not
        if(!roles.contains("CUSTOMER_MANAGER")){
            val customer = customerClient.findCustomer(customerId)
            return customer.username == username && roles.contains("CUSTOMER")
        }
        return true
    }
}