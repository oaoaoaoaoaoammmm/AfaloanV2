package com.example.afaloan.services

import com.example.afaloan.configurations.security.AuthenticationProvider
import com.example.afaloan.controller.auth.dtos.AuthorizeUserResponse
import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.User
import com.example.afaloan.models.UserRole
import com.example.afaloan.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AuthService(
    private val userService: UserService,
    private val encoder: PasswordEncoder,
    private val authProvider: AuthenticationProvider
) {

    @Transactional
    fun register(user: User): UUID {
        if (userService.isExists(user.username)) {
            throw InternalException(httpStatus = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.USER_ALREADY_EXIST)
        }
        logger.info { "Registering user with username - ${user.username}" }
        return userService.create(user.copy(password = encoder.encode(user.password))).id!!
    }

    fun authorize(authUser: User): AuthorizeUserResponse {
        val user = userService.find(authUser.username)
        val encodedAuthUserPassword = encoder.encode(authUser.password)
        if (encoder.matches(user.password, encodedAuthUserPassword)) {
            throw InternalException(httpStatus = HttpStatus.UNAUTHORIZED, errorCode = ErrorCode.USER_PASSWORD_INCORRECT)
        }
        logger.info { "Authorizing user with username - ${user.username}" }
        return createAuthorizeUserResponse(user)
    }

    fun reAuthorize(refreshToken: String): AuthorizeUserResponse {
        if (!authProvider.isValid(refreshToken)) {
            throw InternalException(HttpStatus.UNAUTHORIZED, ErrorCode.TOKEN_EXPIRED)
        }
        val id = UUID.fromString(authProvider.getIdFromToken(refreshToken))
        val user = userService.find(id)
        logger.info { "Refreshing token for user with $id" }
        return createAuthorizeUserResponse(user)
    }

    private fun createAuthorizeUserResponse(user: User): AuthorizeUserResponse {
        return AuthorizeUserResponse(
            id = user.id!!,
            username = user.username,
            access = authProvider.createAccessToken(
                userId = user.id!!,
                username = user.username,
                roles = user.roles.map(UserRole::role).toSet()
            ),
            refresh = authProvider.createRefreshToken(userId = user.id!!, username = user.username)
        )
    }
}