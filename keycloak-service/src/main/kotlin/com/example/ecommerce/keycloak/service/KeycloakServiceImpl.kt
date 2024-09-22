package com.example.ecommerce.keycloak.service

import com.example.ecommerce.customer.dto.CustomerRegistration
import jakarta.ws.rs.NotFoundException
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.ClientsResource
import org.keycloak.admin.client.resource.RolesResource
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.*
import org.springframework.stereotype.Service
import java.util.*


@Service
class KeycloakServiceImpl(private val keycloak: Keycloak) : KeycloakService {

    // Default users config
    private val defaultUserPassword = "default_password"
    private val defaultFirstName = "Default First Name"
    private val defaultLastName = "Default Last Name"

    // Keycloak realm name
    private val realmName = "ecommerce"

    // Keycloak auth url
    private val authUrl = System.getenv("KEYCLOAK_SERVER_URL")

    // Keycloak Clients
    private val clients = mapOf(
        "product_client" to Pair(System.getenv("PRODUCT_SERVICE_CLIENT_ID"), System.getenv("PRODUCT_SERVICE_CLIENT_SECRET")),
        "inventory_client" to Pair(System.getenv("INVENTORY_SERVICE_CLIENT_ID"), System.getenv("INVENTORY_SERVICE_CLIENT_SECRET")),
        "customer_client" to Pair(System.getenv("CUSTOMER_SERVICE_CLIENT_ID"), System.getenv("CUSTOMER_SERVICE_CLIENT_SECRET"))
    )

    // Keycloak Roles
    private val roles = mapOf( "customer_role" to "CUSTOMER",
        "customer_manager_role" to "CUSTOMER_MANAGER",
        "inventory_manager_role" to "INVENTORY_MANAGER",
        "product_manager_role" to "PRODUCT_MANAGER"
    )

    override fun createRealmAndConfigs(){
        createRealmIfNotExists()
        createClientsIfNotExists()
        createRolesIfNotExists()
        createDefaultUsers()
    }

    private fun createRealmIfNotExists() {
        if (keycloak.realms().findAll().stream().noneMatch { r -> r.realm.equals(realmName) }) {
            val realm = RealmRepresentation()
            realm.realm = realmName
            realm.isEnabled = true
            keycloak.realms().create(realm)
            println("[Keycloak Initializer]: Realm $realmName created!")
        }
    }

    private fun createClientsIfNotExists() {
        val clientsResource: ClientsResource? = keycloak.realm(realmName).clients()
        val existingClients: MutableList<ClientRepresentation>? = clientsResource!!.findAll()
        for ((_,value) in clients) {
            if (existingClients!!.stream().noneMatch { c -> c.clientId.equals(value.first) }) {
                val client = ClientRepresentation()
                client.clientId = value.first
                client.isEnabled = true
                client.secret = value.second
                client.isServiceAccountsEnabled = true
                client.isDirectAccessGrantsEnabled = true
                clientsResource.create(client)
                println("[Keycloak Initializer]: Client ${value.first} created!")
            }
        }
    }

    private fun createRolesIfNotExists() {
        val rolesResource: RolesResource? = keycloak.realm(realmName).roles()
        val existingRoles: MutableList<RoleRepresentation>? = rolesResource!!.list()
        for ((_,roleName) in roles) {
            if (existingRoles!!.stream().noneMatch { r -> r.name.equals(roleName) }) {
                val role = RoleRepresentation()
                role.name = roleName
                rolesResource.create(role)
                println("[Keycloak Initializer]: Role $role created!")
            }
        }
    }

    private fun createDefaultUsers() {
        for ((_,roleName) in roles) {
            val username = roleName.lowercase(Locale.getDefault()).replace("_", ".")
            createUser(username, defaultFirstName, defaultLastName, defaultUserPassword, "$username@ecommerce.com", roleName)
        }
    }

    fun createUser(username: String, firstName: String, lastName: String, password: String, email: String, roleName: String): UserRepresentation?{
        val usersResource = keycloak.realm(realmName).users()
        val rolesResource = keycloak.realm(realmName).roles()
        if (usersResource.search(username).isEmpty()) {
            val user = UserRepresentation()
            user.username = username
            user.isEnabled = true
            user.isEmailVerified = true
            user.email= email
            user.firstName = firstName
            user.lastName = lastName
            user.credentials = listOf(CredentialRepresentation().apply {
                type = CredentialRepresentation.PASSWORD
                value = password
                isTemporary = false
            })
            usersResource.create(user)
            val createdUser = usersResource.search(username).first()
            val role = rolesResource.get(roleName).toRepresentation()
            usersResource.get(createdUser.id).roles().realmLevel().add(listOf(role))
            println("[Keycloak Initializer]: User $username with role $roleName created!")
            return createdUser
        }
        return null
    }

    override fun registerCustomer(customerRegistration: CustomerRegistration): UserRepresentation? {
        return createUser(customerRegistration.customer.username, customerRegistration.customer.familyName, customerRegistration.customer.givenName, customerRegistration.password, customerRegistration.customer.email, roles["customer_role"]!!)
    }

    override fun registerCustomerManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["customer_manager_role"]!!)
    }

    override fun registerProductManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["product_manager_role"]!!)
    }

    override fun registerInventoryManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["inventory_manager_role"]!!)
    }

    fun login(username: String, password: String, clientId: String, clientSecret: String): String? {
        try {
            // Create a Keycloak instance to perform the Direct Access Grant (login)
            val keycloakLogin: Keycloak = KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm(realmName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build()

            // Use the Keycloak object to retrieve the access token
            val accessTokenResponse: AccessTokenResponse = keycloakLogin.tokenManager().accessToken

            // Return the access token (or use it in subsequent requests)
            return accessTokenResponse.token

        } catch (ex: Exception) {
            // Handle error (e.g., invalid credentials)
            throw RuntimeException("Login failed for user $username: ${ex.message}")
        }
    }

    override fun loginCustomer(username: String, password: String): String? {
        return clients["customer_client"]?.let { login(username, password, it.first, it.second) }
    }

    override fun removeUser(username: String){
        val userId = findUserIdByUsername(username)
        val usersResource = keycloak.realm(realmName).users()
        usersResource[userId].remove()
    }
    override fun updateCustomer(customerRegistration: CustomerRegistration) {
        val userId = findUserIdByUsername(customerRegistration.customer.username)
        val usersResource = keycloak.realm(realmName).users()

        // Fetch existing user details by user ID
        val existingUser = usersResource[userId].toRepresentation()

        // Update fields
        existingUser.email = customerRegistration.customer.email
        existingUser.credentials = listOf(CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = customerRegistration.password
            isTemporary = false
        })

        // Save the updated user
        usersResource[userId].update(existingUser)
    }

    private fun findUserIdByUsername(username: String): String {
        val usersResource = keycloak.realm(realmName).users()

        val users = usersResource.search(username)
        if (users != null && users.isNotEmpty()) {
            return users[0].id // Assuming username is unique
        }
        throw NotFoundException("User not found")
    }

    override fun loginProduct(username: String, password: String): String? {
        return clients["product_client"]?.let { login(username, password, it.first , it.second) }
    }

    override fun loginInventory(username: String, password: String): String? {
        return clients["inventory_client"]?.let { login(username, password, it.first , it.second) }
    }
}