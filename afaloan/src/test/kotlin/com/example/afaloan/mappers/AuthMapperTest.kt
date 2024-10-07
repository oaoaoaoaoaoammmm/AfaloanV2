package com.example.afaloan.mappers

import com.example.afaloan.controller.auth.dtos.AuthorizeUserRequest
import com.example.afaloan.controller.auth.dtos.RegisterUserRequest
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.services.RoleService
import com.example.afaloan.utils.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class AuthMapperTest {

    private val roleService = mock<RoleService>()

    private val authMapper = AuthMapper(roleService)

    @Test
    fun `convert(request RegisterUserRequest) should execute successfully`() {
        val request = RegisterUserRequest(username = "username", password = "password", roles = listOf(Role.WORKER))
        whenever(roleService.findAll()).thenReturn(USER.roles.toList())

        val result = authMapper.convert(request)

        assertThat(result.id).isNull()
        assertThat(result.username).isEqualTo(request.username)
        assertThat(result.password).isEqualTo(request.password)
        assertThat(result.roles.size).isEqualTo(1)
    }

    @Test
    fun `convert(request AuthorizeUserRequest) should execute successfully`() {
        val request = AuthorizeUserRequest(username = "username", password = "password")

        val result = authMapper.convert(request)

        assertThat(result.username).isEqualTo(request.username)
        assertThat(result.password).isEqualTo(request.password)
    }
}