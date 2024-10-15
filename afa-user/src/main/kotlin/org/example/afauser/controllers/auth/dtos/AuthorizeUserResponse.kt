package org.example.afauser.controllers.auth.dtos

import java.util.*

data class AuthorizeUserResponse(
    val id: UUID,
    val username: String,
    val access: String,
    val refresh: String
)