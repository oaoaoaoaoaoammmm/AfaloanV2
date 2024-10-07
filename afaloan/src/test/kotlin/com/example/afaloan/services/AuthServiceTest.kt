package com.example.afaloan.services

import com.example.afaloan.configurations.security.AuthenticationProvider
import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.utils.UNAUTHORIZED_USER
import com.example.afaloan.utils.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private val userService = mock<UserService>()
    private val encoder = mock<PasswordEncoder>()
    private val authProvider = mock<AuthenticationProvider>()

    private val authService = AuthService(userService, encoder, authProvider)

    @Test
    fun `register should execute successfully`() {
        whenever(userService.find(UNAUTHORIZED_USER.username)).thenReturn(null)
        whenever(encoder.encode(any())).thenReturn("passwordEncoded")
        whenever(userService.create(any())).thenReturn(UNAUTHORIZED_USER)

        val result = authService.register(UNAUTHORIZED_USER)

        assertThat(result).isEqualTo(UNAUTHORIZED_USER.id)
    }

    @Test
    fun `register should throw USER_ALREADY_EXIST`() {
        whenever(userService.isExists(UNAUTHORIZED_USER.username)).thenReturn(true)

        val ex = assertThrows<InternalException> { authService.register(UNAUTHORIZED_USER) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.USER_ALREADY_EXIST)
    }

    @Test
    fun `authorize should execute successfully`() {
        whenever(userService.find(USER.username)).thenReturn(USER)
        whenever(encoder.matches(any(), any())).thenReturn(false)
        whenever(authProvider.createAccessToken(any(), any(), any())).thenReturn("access")
        whenever(authProvider.createRefreshToken(any(), any())).thenReturn("refresh")

        val result = authService.authorize(USER)

        assertThat(result.id).isEqualTo(USER.id)
        assertThat(result.username).isEqualTo(USER.username)
        assertThat(result.access).isNotNull()
        assertThat(result.refresh).isNotNull()
    }

    @Test
    fun `authorize should throw USER_PASSWORD_INCORRECT`() {
        whenever(userService.find(any<String>())).thenReturn(USER)
        whenever(encoder.encode(any())).thenReturn("12345")
        whenever(encoder.matches(any(), any())).thenReturn(true)

        val ex = assertThrows<InternalException> { authService.authorize(USER) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.USER_PASSWORD_INCORRECT)
    }

    @Test
    fun `reAuthorize should execute successfully`() {
        whenever(authProvider.isValid(any())).thenReturn(true)
        whenever(authProvider.getIdFromToken(any())).thenReturn(UNAUTHORIZED_USER.id.toString())
        whenever(userService.find(UNAUTHORIZED_USER.id!!)).thenReturn(UNAUTHORIZED_USER)
        whenever(authProvider.createAccessToken(any(), any(), any())).thenReturn("access")
        whenever(authProvider.createRefreshToken(any(), any())).thenReturn("refresh")

        val result = authService.reAuthorize("refresh")

        assertThat(result.id).isEqualTo(UNAUTHORIZED_USER.id)
        assertThat(result.username).isEqualTo(UNAUTHORIZED_USER.username)
        assertThat(result.access).isEqualTo("access")
        assertThat(result.refresh).isEqualTo("refresh")
    }

    @Test
    fun `reAuthorize should throw TOKEN_EXPIRED`() {
        whenever(authProvider.isValid(any())).thenReturn(false)

        val ex = assertThrows<InternalException> { authService.reAuthorize("refresh") }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.TOKEN_EXPIRED)
    }
}