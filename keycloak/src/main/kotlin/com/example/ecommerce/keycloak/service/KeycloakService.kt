package com.example.ecommerce.keycloak.service

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.ClientsResource
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.RolesResource
import org.keycloak.representations.idm.*
import org.springframework.stereotype.Service
import java.util.*

// Default users config
private const val defaultUserPassword = "default_password"
private const val defaultFirstName = "Default First Name"
private const val defaultLastName = "Default Last Name"

@Service
class KeycloakService(private val keycloak: Keycloak) {

    // Keycloak realm name
    private val realmName = "ecommerce"

    // Keycloak Clients
    private val clients = arrayOf(
        Pair(System.getenv("PRODUCT_SERVICE_CLIENT_ID"), System.getenv("PRODUCT_SERVICE_CLIENT_SECRET")),
        Pair(System.getenv("INVENTORY_SERVICE_CLIENT_ID"), System.getenv("INVENTORY_SERVICE_CLIENT_SECRET")),
        Pair(System.getenv("CUSTOMER_SERVICE_CLIENT_ID"), System.getenv("CUSTOMER_SERVICE_CLIENT_SECRET"))
    )

    // Keycloak Roles
    private val roles = arrayOf( "CUSTOMER",
        "CUSTOMER_MANAGER",
        "INVENTORY_USER",
        "INVENTORY_MANAGER",
        "PRODUCT_USER",
        "PRODUCT_MANAGER"
    )

    fun createRealmAndConfigs(){
        createRealmIfNotExists()
        createClientsIfNotExists()
        createRolesIfNotExists()
        createUsers()
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
        for (cli in clients) {
            if (existingClients!!.stream().noneMatch { c -> c.clientId.equals(cli.first) }) {
                val client = ClientRepresentation()
                client.clientId = cli.first
                client.isEnabled = true
                client.secret = cli.second
                client.isServiceAccountsEnabled = true
                client.isDirectAccessGrantsEnabled = true
                clientsResource.create(client)
                println("[Keycloak Initializer]: Client ${cli.first} created!")
            }
        }
    }

    private fun createRolesIfNotExists() {
        val rolesResource: RolesResource? = keycloak.realm(realmName).roles()
        val existingRoles: MutableList<RoleRepresentation>? = rolesResource!!.list()
        for (roleName in roles) {
            if (existingRoles!!.stream().noneMatch { r -> r.name.equals(roleName) }) {
                val role = RoleRepresentation()
                role.name = roleName
                rolesResource.create(role)
                println("[Keycloak Initializer]: Role $role created!")
            }
        }
    }

    private fun createUsers() {
        val usersResource = keycloak.realm(realmName).users()
        val rolesResource = keycloak.realm(realmName).roles()

        for (roleName in roles) {
            val username = roleName.lowercase(Locale.getDefault()).replace("_", ".")
            if (usersResource.search(username).isEmpty()) {
                val user = UserRepresentation()
                user.username = username
                user.isEnabled = true
                user.isEmailVerified = true
                user.email= "$username@example.com"
                user.firstName = defaultFirstName
                user.lastName = defaultLastName
                user.credentials = listOf(CredentialRepresentation().apply {
                    type = CredentialRepresentation.PASSWORD
                    value = defaultUserPassword
                    isTemporary = false
                })
                usersResource.create(user)
                val createdUser = usersResource.search(username).first()
                val role = rolesResource.get(roleName).toRepresentation()
                usersResource.get(createdUser.id).roles().realmLevel().add(listOf(role))
                println("[Keycloak Initializer]: User $username with role $roleName created!")
            }
        }
    }

    fun assignRoleToUser(username: String, clientId: String, roleName: String) {
        val realmResource: RealmResource = keycloak.realm(realmName)

        val userRepresentation = realmResource.users().searchByUsername(username, true)[0]
        val userResource = realmResource.users()[userRepresentation.id]

        // Search for the client's role
        val clientsResource = realmResource.clients()
        val clientResource = clientsResource[clientId]
        val userRoleResource = clientResource.roles()[roleName]
        val roleRepresentation = userRoleResource.toRepresentation()

        if (roleRepresentation != null) {
            userResource.roles().clientLevel(clientId).add(listOf(roleRepresentation))
            System.out.printf(
                "Assigned to user '%s' the role '%s' with roleId '%s'",
                userRepresentation.username, roleRepresentation.name, roleRepresentation.id
            )
        } else {
            println("Role '$roleName' not found")
        }
    }
}