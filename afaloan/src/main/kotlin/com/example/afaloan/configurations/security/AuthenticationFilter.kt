package com.example.afaloan.configurations.security

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter(
    private val authenticationProvider: AuthenticationProvider
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val bearerToken = request.getHeader(AUTHORIZATION)?.let {
            if (it.startsWith(BEARER_PREFIX)) it.substring(BEARER_PREFIX_LENGTH)
            else throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_DOES_NOT_EXIST)
        }
        bearerToken?.let {
            if (authenticationProvider.isValid(bearerToken)) {
                authenticationProvider.getAuthentication(bearerToken).let {
                    SecurityContextHolder.getContext().authentication = it
                }
            } else {
                throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED)
            }
        }
        filterChain.doFilter(request, response)
    }

    private companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val BEARER_PREFIX_LENGTH = 7
    }
}