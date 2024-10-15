package org.example.afauser.mappers

import org.example.afauser.controllers.auth.dtos.AuthorizeUserRequest
import org.example.afauser.controllers.auth.dtos.RegisterUserRequest
import org.example.afauser.models.User
import org.springframework.stereotype.Component

@Component
class AuthMapper {

    fun convert(request: RegisterUserRequest): User {
        return User(
            username = request.username,
            password = request.password,
            role = request.role
        )
    }

    fun convert(request: AuthorizeUserRequest): User {
        return User(
            username = request.username,
            password = request.password
        )
    }
}