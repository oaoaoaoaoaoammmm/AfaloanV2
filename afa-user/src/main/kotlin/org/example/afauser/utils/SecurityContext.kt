package org.example.afauser.utils

import org.example.afauser.controllers.auth.dtos.AuthorizationDetails
import org.example.afauser.models.enumerations.Role
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import java.util.*

object SecurityContext {

    fun getAuthorizationDetails() = getAuthentication().map { auth ->
        AuthorizationDetails(
            id = auth.id,
            username = auth.username,
            roles = auth.roles.map { Role.valueOf(it) }
        )
    }

    private val Authentication.id: UUID
        get() = UUID.fromString(this.principal as String)

    private val Authentication.username: String
        get() = this.credentials as String

    private val Authentication.roles: List<String>
        get() = this.authorities.map { it.authority }

    private fun getAuthentication() = ReactiveSecurityContextHolder.getContext().map { it.authentication }
}
