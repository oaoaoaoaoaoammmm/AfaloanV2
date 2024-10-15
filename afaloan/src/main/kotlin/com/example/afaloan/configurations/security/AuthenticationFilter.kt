package com.example.afaloan.configurations.security

import com.example.afaloan.utils.toObject
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*

@Component
class AuthenticationFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val userId = request.getHeader(USER_ID)?.let { UUID.fromString(it) }
        val username = request.getHeader(USERNAME)?.takeIf { it.isNotBlank() }
        val userRoles = request.getHeader(USER_ROLES)?.toObject<Set<GrantedAuthority>>()
        if (userId != null && username != null && userRoles != null) {
            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(userId, username, userRoles)
        }
        filterChain.doFilter(request, response)
    }

    private companion object {
        const val USER_ID = "UserId"
        const val USERNAME = "Username"
        const val USER_ROLES = "UserRoles"
    }
}