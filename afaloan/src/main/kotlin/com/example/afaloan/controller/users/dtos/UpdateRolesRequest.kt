package com.example.afaloan.controller.users.dtos

import com.example.afaloan.models.enumerations.Role
import jakarta.validation.constraints.Size

data class UpdateRolesRequest(
    @field:Size(min = 1)
    val roles: List<Role>
)