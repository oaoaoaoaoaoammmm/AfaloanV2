package org.example.afauser.mappers

import org.assertj.core.api.Assertions.assertThat
import org.example.afauser.controllers.auth.dtos.AuthorizeUserRequest
import org.example.afauser.controllers.auth.dtos.RegisterUserRequest
import org.example.afauser.models.enumerations.Role
import org.junit.jupiter.api.Test

class AuthMapperTest {

    private val authMapper = AuthMapper()

    @Test
    fun `convert(request RegisterUserRequest) should execute successfully`() {
        val request = RegisterUserRequest(username = "username", password = "password", role = Role.WORKER)

        val result = authMapper.convert(request)

        assertThat(result.id).isNull()
        assertThat(result.username).isEqualTo(request.username)
        assertThat(result.password).isEqualTo(request.password)
        assertThat(result.role).isEqualTo(request.role)
    }

    @Test
    fun `convert(request AuthorizeUserRequest) should execute successfully`() {
        val request = AuthorizeUserRequest(username = "username", password = "password")

        val result = authMapper.convert(request)

        assertThat(result.username).isEqualTo(request.username)
        assertThat(result.password).isEqualTo(request.password)
    }
}