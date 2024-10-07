package com.example.afaloan.controller.auth

import com.example.afaloan.controller.auth.AuthController.Companion.ROOT_URI
import com.example.afaloan.controller.auth.dtos.AuthorizeUserRequest
import com.example.afaloan.controller.auth.dtos.AuthorizeUserResponse
import com.example.afaloan.controller.auth.dtos.RegisterUserRequest
import com.example.afaloan.controller.auth.dtos.RegisterUserResponse
import com.example.afaloan.mappers.AuthMapper
import com.example.afaloan.services.AuthService
import com.example.afaloan.utils.logger
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ROOT_URI)
class AuthController(
    private val authMapper: AuthMapper,
    private val authService: AuthService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterUserRequest): RegisterUserResponse {
        logger.trace { "Register user with username - ${request.username}" }
        val user = authMapper.convert(request)
        return RegisterUserResponse(authService.register(user))
    }

    @PostMapping(TOKENS)
    @ResponseStatus(HttpStatus.CREATED)
    fun authorize(@Valid @RequestBody request: AuthorizeUserRequest): AuthorizeUserResponse {
        logger.trace { "Authorize user with username - ${request.username}" }
        val user = authMapper.convert(request)
        return authService.authorize(user)
    }

    @PostMapping(TOKENS + REFRESH)
    @ResponseStatus(HttpStatus.CREATED)
    fun reAuthorize(@RequestParam refresh: String): AuthorizeUserResponse {
        logger.trace { "Reauthorize user" }
        return authService.reAuthorize(refresh)
    }

    companion object {
        const val ROOT_URI = "/auth"
        const val TOKENS = "/tokens"
        const val REFRESH = "/refresh"
    }
}