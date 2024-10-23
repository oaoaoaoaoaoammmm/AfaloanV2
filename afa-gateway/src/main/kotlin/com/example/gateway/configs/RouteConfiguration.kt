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
            route(id = "${props.afaUser}-route-auth") {
                uri("lb://${props.afaUser}")
                path("$apiPrefix/auth/**")
                filters { stripPrefix(2) }
            }
            route(id = "${props.afaUser}-route-users") {
                uri("lb://${props.afaUser}")
                path("$apiPrefix/users/**")
                filters {
                    stripPrefix(2)
                    filter(authFilter.apply(AuthenticationFilter.Config))
                }
            }
            route(id = "${props.afaloan}-route") {
                uri("lb://${props.afaloan}")
                path(
                    "$apiPrefix/bids/**",
                    "$apiPrefix/boiling-points/**",
                    "$apiPrefix/microloans/**",
                    "$apiPrefix/profiles/**",
                    "$apiPrefix/processes/**",
                )
                filters {
                    stripPrefix(2)
                    filter(authFilter.apply(AuthenticationFilter.Config))
                }
            }
        }
    }
}