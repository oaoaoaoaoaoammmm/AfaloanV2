package com.example.afaloan.utils

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

fun mockSecurityContext() {
    val securityContext = mock<SecurityContext>()
    val authentication = createTestAuthentication()
    whenever(securityContext.authentication).thenReturn(authentication)
    SecurityContextHolder.setContext(securityContext)
}

private fun createTestAuthentication(): Authentication {
    val auth = mock<UsernamePasswordAuthenticationToken>()
    whenever(auth.principal).thenReturn(USER_ID.toString())
    whenever(auth.credentials).thenReturn(USERNAME)
    whenever(auth.authorities).thenReturn(
        listOf(ROLE_SUPERVISOR, ROLE_WORKER, ROLE_CUSTOMER)
            .map { SimpleGrantedAuthority(it) }
    )
    return auth
}