package com.example.afaloan.controller.auth.dtos

import com.example.afaloan.models.enumerations.Role
import jakarta.validation.constraints.Size

data class RegisterUserRequest(
    @field:Size(min = 9, max = 64)
    val username: String,
    @field:Size(min = 5, max = 60)
    val password: String,
    @field:Size(min = 1)
    val roles: List<Role>
)
