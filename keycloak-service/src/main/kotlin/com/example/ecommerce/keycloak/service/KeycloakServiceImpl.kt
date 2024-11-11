package com.example.ecommerce.keycloak.service

import com.example.ecommerce.keycloak.dto.customer.KeycloakCustomerDTO
import com.example.ecommerce.keycloak.exception.UnauthorizedException
import jakarta.ws.rs.NotFoundException
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.keycloak.OAuth2Constants
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.ClientsResource
import org.keycloak.admin.client.resource.RolesResource
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
        "ecommerce_client" to Pair(System.getenv("ECOMMERCE_CLIENT_ID"), System.getenv("ECOMMERCE_CLIENT_SECRET")),
    )

    // Keycloak Roles
    private val roles = mapOf( "customer_role" to "CUSTOMER",
        "customer_manager_role" to "CUSTOMER_MANAGER",
        "inventory_manager_role" to "INVENTORY_MANAGER",
        "product_manager_role" to "PRODUCT_MANAGER",
        "order_manager_role" to "ORDER_MANAGER"
    )

    override fun createRealmAndConfigs(){
        createRealmIfNotExists()
        configureRealmLoginSettings()
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

    private fun configureRealmLoginSettings() {
        val realmResource = keycloak.realm(realmName)
        val realmRepresentation = realmResource.toRepresentation()
        // Check current settings
        val isEditUsernameAllowed = realmRepresentation.isEditUsernameAllowed
        // Update settings if needed
        if (!isEditUsernameAllowed) {
            realmRepresentation.isEditUsernameAllowed = true
            println("Updating realm login settings to allow username editing.")
            // Persist the updated settings
            realmResource.update(realmRepresentation)
            println("Realm login settings updated.")
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

    @Transactional
    override fun registerCustomer(keycloakCustomer: KeycloakCustomerDTO): UserRepresentation? {
        return createUser(keycloakCustomer.username, keycloakCustomer.familyName!!, keycloakCustomer.givenName!!, keycloakCustomer.password, keycloakCustomer.email, roles["customer_role"]!!)
    }

    @Transactional
    override fun registerCustomerManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["customer_manager_role"]!!)
    }

    @Transactional
    override fun registerProductManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["product_manager_role"]!!)
    }

    @Transactional
    override fun registerInventoryManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["inventory_manager_role"]!!)
    }

    @Transactional
    override fun registerOrderManager(username: String, familyName: String, givenName: String, password: String, email: String): UserRepresentation? {
        return createUser(username, familyName, givenName, password, email, roles["order_manager_role"]!!)
    }

    override fun login(username: String, password: String): String {
         try {
            val keycloakLogin: Keycloak = KeycloakBuilder.builder()
                .serverUrl(authUrl)
                .realm(realmName)
                .clientId(clients["ecommerce_client"]!!.first)
                .clientSecret(clients["ecommerce_client"]!!.second)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build()
            val accessTokenResponse: AccessTokenResponse = keycloakLogin.tokenManager().accessToken
            return accessTokenResponse.token
        }catch (e: WebApplicationException) {
            if (e.response.status == Response.Status.UNAUTHORIZED.statusCode) {
                throw UnauthorizedException("Invalid credentials provided: ${e.response.readEntity(String::class.java)}")
            } else {
                throw RuntimeException("Failed to authenticate: ${e.message}")
            }
        } catch (e: Exception) {
            throw RuntimeException("An unexpected error occurred: ${e.message}")
        }
    }

    @Transactional
    override fun removeUser(username: String){
        val userId = findUserIdByUsername(username)
        val usersResource = keycloak.realm(realmName).users()
        usersResource[userId].remove()
    }

    @Transactional
    override fun updateCustomer(keycloakCustomer: KeycloakCustomerDTO) {
        val userId = findUserIdByUsername(keycloakCustomer.username)
        val usersResource = keycloak.realm(realmName).users()

        // Fetch existing user details by user ID
        val existingUser = usersResource[userId].toRepresentation()

        // Update fields
        if(keycloakCustomer.updatedUsername != null)
            existingUser.username = keycloakCustomer.updatedUsername
        else
            existingUser.username = keycloakCustomer.username
        existingUser.email = keycloakCustomer.email
        existingUser.credentials = listOf(CredentialRepresentation().apply {
            type = CredentialRepresentation.PASSWORD
            value = keycloakCustomer.password
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
}