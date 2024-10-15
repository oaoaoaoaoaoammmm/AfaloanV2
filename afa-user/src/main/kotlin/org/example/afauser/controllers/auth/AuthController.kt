package org.example.afauser.controllers.auth

import jakarta.validation.Valid
import org.example.afauser.controllers.auth.AuthController.Companion.ROOT_URI
import org.example.afauser.controllers.auth.dtos.*
import org.example.afauser.mappers.AuthMapper
import org.example.afauser.services.AuthService
import org.example.afauser.services.UserService
import org.example.afauser.utils.SecurityContext
import org.example.afauser.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping(ROOT_URI)
class AuthController(
    private val authMapper: AuthMapper,
    private val authService: AuthService,
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: Mono<RegisterUserRequest>): Mono<RegisterUserResponse> {
        return request.map(authMapper::convert)
            .flatMap { user ->
                logger.trace { "Register user with username - ${user.username}" }
                authService.register(user)
            }.map { RegisterUserResponse(it) }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    fun getAuthDetails(): Mono<AuthorizationDetails> {
        logger.trace { "Get authorization details for user - ${SecurityContext.getAuthorizedUserUsername()}" }
        return authService.getAuthDetails()
    }

    @PostMapping(TOKENS)
    @ResponseStatus(HttpStatus.CREATED)
    fun authorize(@Valid @RequestBody request: Mono<AuthorizeUserRequest>): Mono<AuthorizeUserResponse> {
        return request.map(authMapper::convert)
            .flatMap { auth ->
                logger.trace { "Authorization user with username - ${auth.username}" }
                authService.authorize(auth)
            }
    }

    @PostMapping(TOKENS + REFRESH)
    @ResponseStatus(HttpStatus.CREATED)
    fun reAuthorize(@RequestParam refresh: String): Mono<AuthorizeUserResponse> {
        logger.trace { "Reauthorize user" }
        return authService.reAuthorize(refresh)
    }

    companion object {
        const val ROOT_URI = "/auth"
        const val TOKENS = "/tokens"
        const val REFRESH = "/refresh"
    }
}