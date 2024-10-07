package com.example.afaloan.mappers

import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.services.RoleService
import com.example.afaloan.utils.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class RoleMapperTest {

    private val roleService = mock<RoleService>()

    private val roleMapper = RoleMapper(roleService)

    @Test
    fun `convert should execute successfully`() {
        whenever(roleService.findAll()).thenReturn(USER.roles.toList())

        val result = roleMapper.convert(listOf(Role.WORKER))

        assertThat(result.size).isEqualTo(1)
    }
}