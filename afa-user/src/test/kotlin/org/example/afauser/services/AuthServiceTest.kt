package org.example.afauser.services

import org.assertj.core.api.Assertions.assertThat
import org.example.afauser.configurations.security.AuthenticationProvider
import org.example.afauser.controllers.auth.dtos.AuthorizeUserResponse
import org.example.afauser.exceptions.ErrorCode
import org.example.afauser.exceptions.InternalException
import org.example.afauser.utils.UNAUTHORIZED_USER
import org.example.afauser.utils.USER
import org.example.afauser.utils.mockSecurityContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier

class AuthServiceTest {

    private val userService = mock<UserService>()
    private val encoder = mock<PasswordEncoder>()
    private val authProvider = mock<AuthenticationProvider>()

    private val authService = AuthService(userService, encoder, authProvider)

    @BeforeEach
    fun setUp() = mockSecurityContext()

    @AfterEach
    fun tearDown() = SecurityContextHolder.clearContext()

    @Test
    fun `register should execute successfully`() {
        whenever(userService.isExists(UNAUTHORIZED_USER.username)).thenReturn(Mono.just(false))
        whenever(encoder.encode(any())).thenReturn("passwordEncoded")
        whenever(userService.create(any())).thenReturn(Mono.just(UNAUTHORIZED_USER))

        StepVerifier.create(authService.register(UNAUTHORIZED_USER))
            .expectNext(UNAUTHORIZED_USER.id)
            .expectComplete()
            .verify()
    }


    @Test
    fun `register should throw USER_ALREADY_EXIST`() {
        whenever(userService.isExists(UNAUTHORIZED_USER.username)).thenReturn(Mono.just(true))

        StepVerifier.create(authService.register(UNAUTHORIZED_USER))
            .expectError(InternalException::class)
            .verify()
    }

    @Test
    fun `authorize should execute successfully`() {
        whenever(userService.find(USER.username)).thenReturn(Mono.just(USER))
        whenever(encoder.matches(any(), any())).thenReturn(false)
        whenever(authProvider.createAccessToken(any(), any(), any())).thenReturn("access")
        whenever(authProvider.createRefreshToken(any(), any())).thenReturn("refresh")

        StepVerifier.create(authService.authorize(USER))
            .expectNext(AuthorizeUserResponse(USER.id!!, USER.username, "access", "refresh"))
            .expectComplete()
            .verify()
    }

    @Test
    fun `authorize should throw USER_PASSWORD_INCORRECT`() {
        whenever(userService.find(any<String>())).thenReturn(Mono.just(USER))
        whenever(encoder.encode(any())).thenReturn("12345")
        whenever(encoder.matches(any(), any())).thenReturn(true)

        StepVerifier.create(authService.authorize(UNAUTHORIZED_USER))
            .expectError(InternalException::class)
            .verify()
    }

    @Test
    fun `reAuthorize should execute successfully`() {
        whenever(authProvider.isValid(any())).thenReturn(true)
        whenever(authProvider.getIdFromToken(any())).thenReturn(UNAUTHORIZED_USER.id.toString())
        whenever(userService.find(UNAUTHORIZED_USER.id!!)).thenReturn(Mono.just(UNAUTHORIZED_USER))
        whenever(authProvider.createAccessToken(any(), any(), any())).thenReturn("access")
        whenever(authProvider.createRefreshToken(any(), any())).thenReturn("refresh")


        StepVerifier.create(authService.reAuthorize("refresh"))
            .expectNext(AuthorizeUserResponse(
                UNAUTHORIZED_USER.id!!, UNAUTHORIZED_USER.username, "access", "refresh")
            )
            .expectComplete()
            .verify()
    }

    @Test
    fun `reAuthorize should throw TOKEN_EXPIRED`() {
        whenever(authProvider.isValid(any())).thenReturn(false)

        val ex = assertThrows<InternalException> { authService.reAuthorize("refresh") }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.TOKEN_EXPIRED)
    }
}