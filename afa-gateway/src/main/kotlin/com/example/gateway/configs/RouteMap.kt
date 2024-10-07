package com.example.gateway.configs

import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteMap {

    @Bean
    fun routeLocator(
        route: RouteLocatorBuilder,
        @Value("\${server.api.prefix}") apiPrefix: String,
    ) = route.routes {
        route(id = "afaloan-route") {
            path("$apiPrefix/**")
            filters { stripPrefix(2) }
            uri("lb://afaloan")
        }
    }
}