package com.example.afaloan.controller.auth.dtos

import java.util.UUID

data class AuthorizeUserResponse(
    val id: UUID,
    val username: String,
    val access: String,
    val refresh: String
)