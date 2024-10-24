package org.example.afauser.controllers.users.dtos

import org.example.afauser.models.enumerations.Role

data class UpdateRoleRequest(
    val role: Role
)
