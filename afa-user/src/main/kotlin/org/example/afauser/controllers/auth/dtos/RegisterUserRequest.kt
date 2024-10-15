package org.example.afauser.controllers.auth.dtos

import jakarta.validation.constraints.Size
import org.example.afauser.models.enumerations.Role

data class RegisterUserRequest(
    @field:Size(min = 9, max = 64)
    val username: String,
    @field:Size(min = 5, max = 60)
    val password: String,
    val role: Role
)
