package com.fitcore.payment.config

import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.HttpRequestInterceptor
import org.apache.hc.core5.http.HttpResponseInterceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val client = HttpClients.custom()
            .addRequestInterceptorLast(HttpRequestInterceptor { request, entity, context ->
                println("HTTP REQUEST: ${request.method} ${request.requestUri}")
                request.headers.forEach { header ->
                    println("HEADER: ${header.name}: ${header.value}")
                }
            })
            .addResponseInterceptorLast(HttpResponseInterceptor { response, entity, context ->
                println("HTTP RESPONSE: ${response.code} ${response.reasonPhrase}")
                response.headers.forEach { header ->
                    println("HEADER: ${header.name}: ${header.value}")
                }
            })
            .build()

        val factory = HttpComponentsClientHttpRequestFactory(client)
        return RestTemplate(factory)
    }
}
