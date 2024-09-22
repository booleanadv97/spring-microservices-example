package com.example.ecommerce.inventory.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt

class KeycloakRoleConverter : Converter<Jwt, Collection<GrantedAuthority>> {

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        val authorities = mutableSetOf<GrantedAuthority>()

        val realmRoles = jwt.claims["realm_access"] as Map<*, *>?
        realmRoles?.get("roles")?.let { roles ->
            (roles as List<*>).forEach { role ->
                authorities.add(SimpleGrantedAuthority("ROLE_$role"))
            }
        }
        return authorities
    }
}
