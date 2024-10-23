package org.example.afauser.services

import org.example.afauser.configurations.security.AuthenticationProvider
import org.example.afauser.controllers.auth.dtos.AuthorizationDetails
import org.example.afauser.controllers.auth.dtos.AuthorizeUserResponse
import org.example.afauser.exceptions.ErrorCode
import org.example.afauser.exceptions.InternalException
import org.example.afauser.models.User
import org.example.afauser.utils.SecurityContext
import org.example.afauser.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*

@Service
class AuthService(
    private val userService: UserService,
    private val encoder: PasswordEncoder,
    private val authProvider: AuthenticationProvider
) {

    @Transactional
    fun register(user: User): Mono<UUID> {
        return userService.isExists(user.username).flatMap { result ->
            if (result) {
                return@flatMap Mono.error(
                    InternalException(httpStatus = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.USER_ALREADY_EXIST)
                )
            }
            logger.info { "Registering user with username - ${user.username}" }
            userService.create(user.copy(password = encoder.encode(user.password))).mapNotNull { it.id }
        }
    }

    fun getAuthDetails(): Mono<AuthorizationDetails> {
        logger.info { "Getting authorization details..." }
        return SecurityContext.getAuthorizationDetails()
    }

    fun authorize(authUser: User): Mono<AuthorizeUserResponse> {
        return userService.find(authUser.username).flatMap { user ->
            val encodedAuthUserPassword = encoder.encode(authUser.password)
            if (encoder.matches(user.password, encodedAuthUserPassword)) {
                return@flatMap Mono.error(
                    InternalException(
                        httpStatus = HttpStatus.UNAUTHORIZED,
                        errorCode = ErrorCode.USER_PASSWORD_INCORRECT
                    )
                )
            }
            logger.info { "Authorizing user with username - ${user.username}" }
            createAuthorizeUserResponse(user)
        }
    }

    fun reAuthorize(refreshToken: String): Mono<AuthorizeUserResponse> {
        if (!authProvider.isValid(refreshToken)) {
            throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED)
        }
        val id = UUID.fromString(authProvider.getIdFromToken(refreshToken))
        return userService.find(id).flatMap { user ->
            logger.info { "Refreshing token for user with $id" }
            createAuthorizeUserResponse(user)
        }
    }

    private fun createAuthorizeUserResponse(user: User): Mono<AuthorizeUserResponse> {
        return Mono.just(
            AuthorizeUserResponse(
                id = user.id!!,
                username = user.username,
                access = authProvider.createAccessToken(
                    userId = user.id,
                    username = user.username,
                    roles = setOf(user.role)
                ),
                refresh = authProvider.createRefreshToken(userId = user.id, username = user.username)
            )
        )
    }
}