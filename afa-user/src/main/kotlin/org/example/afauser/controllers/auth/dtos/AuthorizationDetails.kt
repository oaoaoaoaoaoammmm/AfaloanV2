package org.example.afauser.controllers.auth.dtos

import org.example.afauser.models.enumerations.Role
import java.util.UUID

data class AuthorizationDetails(
    val id: UUID,
    val username: String,
    val roles: List<Role>
)
