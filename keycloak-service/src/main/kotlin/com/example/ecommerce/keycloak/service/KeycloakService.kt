package com.example.ecommerce.keycloak.service

import com.example.ecommerce.keycloak.dto.customer.KeycloakCustomerDTO
import org.keycloak.representations.idm.UserRepresentation

interface KeycloakService {
    //Initializer
    fun createRealmAndConfigs()

    //Customer keycloak services
    fun registerCustomer(keycloakCustomer: KeycloakCustomerDTO): UserRepresentation?
    fun registerCustomerManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?
    fun login(username: String, password: String): String
    fun updateCustomer(keycloakCustomer: KeycloakCustomerDTO)

    //Product keycloak services
    fun registerProductManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?

    //Inventory keycloak services
    fun registerInventoryManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?
    //Order keycloak services
    fun registerOrderManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation?

    fun removeUser(username: String)
}