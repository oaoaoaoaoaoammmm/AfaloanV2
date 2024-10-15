package org.example.afauser.controllers.users

import jakarta.validation.Valid
import org.example.afauser.controllers.users.UserController.Companion.ROOT_URI
import org.example.afauser.controllers.users.dtos.UpdateRoleRequest
import org.example.afauser.controllers.users.dtos.UserDto
import org.example.afauser.mappers.UserMapper
import org.example.afauser.services.UserService
import org.example.afauser.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestParam
import reactor.core.publisher.Mono
import java.util.*

@RestController
@RequestMapping(ROOT_URI)
class UserController(
    private val userMapper: UserMapper,
    private val userService: UserService
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): Mono<UserDto> {
        logger.trace { "Find user by id - $id" }
        return userService.find(id).map(userMapper::convert)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun exists(@RequestParam username: String): Mono<Boolean> {
        logger.trace { "Exists user by username: $username" }
        return userService.isExists(username)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID): Mono<Void> {
        logger.trace { "Delete user with id - $id" }
        return userService.delete(id)
    }

    @PatchMapping("/{id}/$ROLES")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    fun updateRole(
        @PathVariable id: UUID,
        @Valid @RequestBody request: Mono<UpdateRoleRequest>
    ): Mono<Void> {
        return request.flatMap { newRole ->
            logger.trace { "Update roles for user with id - $id" }
            userService.updateRoles(id, newRole.role).let { Mono.empty() }
        }
    }

    @PatchMapping("/{id}/$BLOCK")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun block(@PathVariable id: UUID): Mono<Void> {
        logger.trace { "Block user by id - $id" }
        return userService.block(id).let { Mono.empty() }
    }

    @DeleteMapping("/{id}/$BLOCK")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun unblock(@PathVariable id: UUID): Mono<Void> {
        logger.trace { "Unblock user with id - $id" }
        return userService.unblock(id).let { Mono.empty() }
    }

    @PatchMapping("/{id}/$CONFIRM")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun confirm(@PathVariable id: UUID): Mono<Void> {
        logger.trace { "Confirm user with id - $id" }
        return userService.confirm(id).let { Mono.empty() }
    }

    companion object {
        const val ROOT_URI = "/users"
        const val ROLES = "roles"
        const val BLOCK = "block"
        const val CONFIRM = "confirm"
    }
}