package org.example.afauser.utils

import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.test.context.TestSecurityContextHolder

fun mockSecurityContext() {
    val securityContext = mock<SecurityContext>()
    val authentication = createMockAuthentication()
    whenever(securityContext.authentication).thenReturn(authentication)
    TestSecurityContextHolder.setContext(securityContext)
}

fun createMockAuthentication(): Authentication {
    val auth = mock<UsernamePasswordAuthenticationToken>()
    whenever(auth.principal).thenReturn(USER.id!!.toString())
    whenever(auth.credentials).thenReturn(USER.username)
    whenever(auth.authorities).thenReturn(listOf(SimpleGrantedAuthority(USER.role.name)))
    return auth
}

fun createAuthentication(): Authentication {
    return UsernamePasswordAuthenticationToken(
        USER.id!!.toString(),
        USER.username,
        listOf(SimpleGrantedAuthority(USER.role.name))
    )
}