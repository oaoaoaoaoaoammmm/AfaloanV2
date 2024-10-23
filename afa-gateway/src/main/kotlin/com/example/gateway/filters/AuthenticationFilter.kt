package com.example.gateway.filters

import com.example.gateway.configs.ServiceUrlsProperties
import com.example.gateway.dtos.AuthorizationDetails
import com.example.gateway.utils.logger
import com.example.gateway.utils.toJson
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class AuthenticationFilter(
    private val client: WebClient.Builder,
    private val props: ServiceUrlsProperties
) : AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            logger.debug { "Processing auth request - ${exchange.request.uri.path}" }
            val bearerToken = exchange.request.headers.getFirst("Authorization")
            return@GatewayFilter if (bearerToken != null) {
                client.build().get()
                    .uri("lb://${props.afaUser}/auth")
                    .accept(MediaType.APPLICATION_JSON)
                    .header(AUTHORIZATION, bearerToken)
                    .retrieve()
                    .bodyToMono(AuthorizationDetails::class.java)
                    .flatMap { response ->
                        val mutableRequest = exchange.request.mutate()
                            .header(USER_ID, response.id.toString())
                            .header(USERNAME, response.username)
                            .header(USER_ROLES, response.roles.toJson())
                            .build()
                        chain.filter(exchange.mutate().request(mutableRequest).build())
                    }
            } else {
                chain.filter(exchange)
            }
        }
    }

    companion object Config {
        const val USER_ID = "UserId"
        const val USERNAME = "Username"
        const val USER_ROLES = "UserRoles"
        const val AUTHORIZATION = "Authorization"
    }
}