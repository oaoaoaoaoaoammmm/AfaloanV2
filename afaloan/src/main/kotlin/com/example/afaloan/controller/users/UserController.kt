package com.example.afaloan.controller.users

import com.example.afaloan.controller.users.UserController.Companion.ROOT_URI
import com.example.afaloan.controller.users.dtos.UpdateRolesRequest
import com.example.afaloan.controller.users.dtos.UserDto
import com.example.afaloan.mappers.RoleMapper
import com.example.afaloan.mappers.UserMapper
import com.example.afaloan.services.UserService
import com.example.afaloan.utils.logger
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(ROOT_URI)
class UserController(
    private val userMapper: UserMapper,
    private val roleMapper: RoleMapper,
    private val userService: UserService
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): UserDto {
        logger.trace { "Find user by id - $id" }
        return userService.find(id).let { userMapper.convert(it) }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: UUID) {
        logger.trace { "Delete user with id - $id" }
        return userService.delete(id)
    }

    @PatchMapping("/{id}/$ROLES")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('SUPERVISOR')")
    fun updateRoles(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateRolesRequest
    ) {
        logger.trace { "Update roles for user with id - $id" }
        val newUserRoles = roleMapper.convert(request.roles)
        userService.updateRoles(id, newUserRoles)
    }

    @PatchMapping("/{id}/$BLOCK")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun block(@PathVariable id: UUID) {
        logger.trace { "Block user by id - $id" }
        userService.block(id)
    }

    @DeleteMapping("/{id}/$BLOCK")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun unblock(@PathVariable id: UUID) {
        logger.trace { "Unblock user with id - $id" }
        userService.unblock(id)
    }

    @PatchMapping("/{id}/$CONFIRM")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun confirm(@PathVariable id: UUID) {
        logger.trace { "Confirm user with id - $id" }
        userService.confirm(id)
    }

    companion object {
        const val ROOT_URI = "/users"
        const val ROLES = "roles"
        const val BLOCK = "block"
        const val CONFIRM = "confirm"
    }
}