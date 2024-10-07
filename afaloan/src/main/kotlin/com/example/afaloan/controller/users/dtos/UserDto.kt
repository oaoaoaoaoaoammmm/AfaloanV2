package com.example.afaloan.controller.users.dtos

import com.example.afaloan.models.enumerations.Role
import java.util.UUID

data class UserDto(
    val id: UUID,
    val username: String,
    val confirmed: Boolean,
    val confirmedUsername: Boolean,
    val blocked: Boolean,
    val roles: List<Role>
)
