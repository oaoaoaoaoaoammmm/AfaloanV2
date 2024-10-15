package com.example.gateway.dtos

import java.util.*

data class AuthorizationDetails(
    val id: UUID,
    val username: String,
    val roles: List<String>
)
