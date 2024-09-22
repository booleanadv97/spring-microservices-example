package com.example.ecommerce.cart.config

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component


@Component
class FeignClientInterceptor : RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val authentication = SecurityContextHolder.getContext().authentication
        if (authentication is JwtAuthenticationToken) {
            val token = authentication.token.tokenValue
            template.header("Authorization", "Bearer $token")
        }
    }
}