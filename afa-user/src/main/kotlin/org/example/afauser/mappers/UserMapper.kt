package org.example.afauser.mappers

import org.example.afauser.controllers.users.dtos.UserDto
import org.example.afauser.models.User
import org.springframework.stereotype.Component

@Component
class UserMapper {

    fun convert(user: User): UserDto {
        return UserDto(
            id = user.id!!,
            username = user.username,
            confirmed = user.confirmed,
            confirmedUsername = user.confirmedUsername,
            blocked = user.blocked,
            role = user.role
        )
    }
}