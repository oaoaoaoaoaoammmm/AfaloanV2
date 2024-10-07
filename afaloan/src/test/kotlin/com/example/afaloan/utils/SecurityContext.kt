package com.example.afaloan.utils

import com.example.afaloan.models.UserRole
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder

fun mockSecurityContext() {
    val securityContext = mock<SecurityContext>()
    val authentication = createTestAuthentication()
    whenever(securityContext.authentication).thenReturn(authentication)
    SecurityContextHolder.setContext(securityContext)
}

private fun createTestAuthentication(): Authentication {
    val auth = mock<UsernamePasswordAuthenticationToken>()
    whenever(auth.principal).thenReturn(USER.id!!.toString())
    whenever(auth.credentials).thenReturn(USER.username)
    whenever(auth.authorities).thenReturn(
        USER.roles.map(UserRole::role)
            .map { SimpleGrantedAuthority(it.name) }
    )
    return auth
}