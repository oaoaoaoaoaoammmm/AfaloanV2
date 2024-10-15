package com.example.gateway.filters

import com.example.gateway.configs.ServiceUrlsProperties
import com.example.gateway.dtos.AuthorizationDetails
import com.example.gateway.utils.logger
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry
import java.time.Duration

@Component
class AuthenticationFilter(
    private val client: WebClient,
    private val props: ServiceUrlsProperties
) : AbstractGatewayFilterFactory<AuthenticationFilter.Config>(Config::class.java) {

    override fun apply(config: Config): GatewayFilter {
        return GatewayFilter { exchange, chain ->
            logger.debug { "Processing auth request - ${exchange.request.uri.path}" }
            val bearerToken = exchange.request.headers.getFirst("Authorization")
            bearerToken?.let { bearer ->
                client.get()
                    .uri("lb://${props.userService}/auth")
                    .header(AUTHORIZATION, bearer)
                    .retrieve()
                    .bodyToMono(AuthorizationDetails::class.java)
                    .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                    .map { response ->
                        val mutableRequest = exchange.request.mutate()
                            .header(USER_ID, response.id.toString())
                            .header(USERNAME, response.username)
                            .header(USER_ROLES, response.roles.toString())
                            .build()
                        return@map exchange.mutate().request(mutableRequest).build()
                    }
                    .flatMap { chain.filter(it) }
            }
            return@GatewayFilter chain.filter(exchange)
        }
    }

    companion object Config {
        const val USER_ID = "UserId"
        const val USERNAME = "Username"
        const val USER_ROLES = "UserRoles"
        const val AUTHORIZATION = "Authorization"
    }
}