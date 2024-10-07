package com.example.afaloan.controller.users

import com.example.afaloan.controller.users.RoleController.Companion.ROOT_URI
import com.example.afaloan.models.UserRole
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.services.RoleService
import com.example.afaloan.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ROOT_URI)
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun findAll(): Collection<Role> {
        logger.trace { "Find all roles" }
        return roleService.findAll().map(UserRole::role)
    }

    companion object {
        const val ROOT_URI = "/roles"
    }
}