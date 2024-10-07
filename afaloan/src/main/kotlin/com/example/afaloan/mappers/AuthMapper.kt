package com.example.afaloan.mappers

import com.example.afaloan.controller.auth.dtos.AuthorizeUserRequest
import com.example.afaloan.controller.auth.dtos.RegisterUserRequest
import com.example.afaloan.models.User
import com.example.afaloan.services.RoleService
import org.springframework.stereotype.Component

@Component
class AuthMapper(
    private val roleService: RoleService
) {

    fun convert(request: RegisterUserRequest): User {
        return User(
            username = request.username,
            password = request.password,
            roles = roleService.findAll().filter {
                request.roles.contains(it.role)
            }.toSet()
        )
    }

    fun convert(request: AuthorizeUserRequest): User {
        return User(
            username = request.username,
            password = request.password,
            roles = setOf()
        )
    }
}