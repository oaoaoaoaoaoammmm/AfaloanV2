package org.example.afauser.controllers.users.dtos

import org.example.afauser.models.enumerations.Role
import java.util.*

data class UserDto(
    val id: UUID,
    val username: String,
    val confirmed: Boolean,
    val confirmedUsername: Boolean,
    val blocked: Boolean,
    val role: Role
)
