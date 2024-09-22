package com.example.ecommerce.keycloak.service

import com.example.ecommerce.customer.dto.CustomerRegistration
import org.keycloak.representations.idm.UserRepresentation

interface KeycloakService {
    //Initializer
    fun createRealmAndConfigs()

    //Customer keycloak services
    fun registerCustomer(customerRegistration: CustomerRegistration): UserRepresentation?
    fun registerCustomerManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?
    fun loginCustomer(username: String, password: String): String?
    fun updateCustomer(customerRegistration: CustomerRegistration)

    //Product keycloak services
    fun registerProductManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?
    fun loginProduct(username: String, password: String): String?

    //Inventory keycloak services
    fun registerInventoryManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?
    fun loginInventory(username: String, password: String): String?

    fun removeUser(username: String)
}