package com.example.ecommerce.customer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableFeignClients
class CustomerServiceApplication

fun main(args: Array<String>) {
    runApplication<CustomerServiceApplication>(*args)
}
