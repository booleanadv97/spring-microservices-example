package com.example.ecommerce.order.config

import com.example.ecommerce.order.exception.NotFoundException
import com.example.ecommerce.order.exception.UnauthorizedException
import feign.Response
import feign.codec.ErrorDecoder
import feign.FeignException
import org.apache.commons.io.IOUtils
import java.nio.charset.StandardCharsets

class CustomFeignErrorDecoder : ErrorDecoder {

    override fun decode(methodKey: String, response: Response): Exception {
        val responseBody = response.body()
        val errorMessage = responseBody?.let { IOUtils.toString(it.asInputStream(), StandardCharsets.UTF_8) }

        println("Received response status: ${response.status()}")
        println("Error message: $errorMessage")

        when (response.status()) {
            401 -> throw UnauthorizedException(errorMessage ?: "Unauthorized")
            404 -> throw NotFoundException(errorMessage ?: "Not found")
            500 -> throw RuntimeException(errorMessage ?: "Internal Server Error")
            else -> throw FeignException.errorStatus(methodKey, response)
        }
    }
}
