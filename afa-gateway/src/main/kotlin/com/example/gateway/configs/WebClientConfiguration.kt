package com.example.gateway.configs

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    @Primary
    @LoadBalanced
    fun webClient(): WebClient.Builder {
        return WebClient.builder()
    }
}