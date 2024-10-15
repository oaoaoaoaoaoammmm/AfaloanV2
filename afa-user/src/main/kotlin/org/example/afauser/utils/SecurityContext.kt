package org.example.afauser.utils

import org.example.afauser.exceptions.ErrorCode
import org.example.afauser.exceptions.InternalException
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

object SecurityContext {

    fun getAuthorizedUserId() = getAuthentication()?.id
        ?: throw InternalException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN)

    fun getAuthorizedUserUsername() = getAuthentication()?.username
        ?: throw InternalException(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN)

    fun getAuthorizedUserRoles() = getAuthentication().roles

    private val Authentication.id: UUID
        get() = UUID.fromString(this.principal as String)

    private val Authentication.username: String
        get() = this.credentials as String

    private val Authentication.roles: List<String>
        get() = this.authorities.map { it.authority }

    private fun getAuthentication() = SecurityContextHolder.getContext().authentication
}
