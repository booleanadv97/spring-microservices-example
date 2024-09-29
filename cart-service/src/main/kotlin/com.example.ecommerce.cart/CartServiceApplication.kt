package com.example.ecommerce.cart

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["com.example.ecommerce.common", "com.example.ecommerce.cart"])
@EnableFeignClients
class CartServiceApplication
fun main(args: Array<String>) {
    runApplication<CartServiceApplication>(*args)
}
