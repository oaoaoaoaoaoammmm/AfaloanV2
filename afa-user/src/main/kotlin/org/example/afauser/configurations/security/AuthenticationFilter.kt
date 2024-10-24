package org.example.afauser.configurations.security

import org.example.afauser.exceptions.ErrorCode
import org.example.afauser.exceptions.InternalException
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class AuthenticationFilter(
    private val authenticationProvider: AuthenticationProvider
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val bearerToken = exchange.request.headers[AUTHORIZATION]?.let { list ->
            list.firstOrNull()?.let {
                if (it.startsWith(BEARER_PREFIX)) it.substring(BEARER_PREFIX_LENGTH)
                else throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_DOES_NOT_EXIST)
            }
        }
        val auth = bearerToken?.let {
            if (authenticationProvider.isValid(bearerToken)) {
                authenticationProvider.getAuthentication(bearerToken)
            } else {
                throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED)
            }
        }
        return if (auth != null) {
            chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth))
        } else {
            chain.filter(exchange)
        }
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val BEARER_PREFIX_LENGTH = 7
    }
}