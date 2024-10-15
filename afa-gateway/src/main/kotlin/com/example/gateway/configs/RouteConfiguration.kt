package com.example.gateway.configs

import com.example.gateway.filters.AuthenticationFilter
import org.springframework.beans.factory.annotation.Value
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RouteConfiguration {

    @Bean
    fun routeLocator(
        route: RouteLocatorBuilder,
        props: ServiceUrlsProperties,
        authFilter: AuthenticationFilter,
        @Value("\${server.api.prefix}") apiPrefix: String,
    ): RouteLocator {
        return route.routes {
            route(id = "${props.afaloanService}-route") {
                path("$apiPrefix/**")
                uri("lb://${props.afaloanService}")
                filters {
                    stripPrefix(apiPrefix.split("/").size)
                    filter(authFilter.apply(AuthenticationFilter.Config))
                }
            }
        }
    }
}