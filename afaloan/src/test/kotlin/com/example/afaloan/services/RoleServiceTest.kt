package com.example.afaloan.services

import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.repositories.RoleRepository
import com.example.afaloan.utils.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.collections.Set

class RoleServiceTest {

    private val roleRepository = mock<RoleRepository>()
    private val roleService = RoleService(roleRepository)

    @Test
    fun `findAll should execute successfully`() {
        whenever(roleRepository.findAll()).thenReturn(USER.roles.toList())

        val result = roleService.findAll()

        assertThat(result).isEqualTo(USER.roles.toList())
    }

    @ParameterizedTest
    @MethodSource("positiveCasesSourceForSingleArg")
    fun `isExists(role Role) should return true`(role: Role) {
        whenever(roleRepository.findAll()).thenReturn(USER.roles.toList())

        val result = roleService.isExists(role)

        assertThat(result).isTrue()
    }

    @ParameterizedTest
    @MethodSource("positiveCasesSourceForSetArg")
    fun `isExists(roles Set) should return true`(roles: Set<Role>) {
        whenever(roleRepository.findAll()).thenReturn(USER.roles.toList())

        val result = roleService.isExists(roles)

        assertThat(result).isTrue()
    }

    companion object {

        @JvmStatic
        fun positiveCasesSourceForSingleArg() = listOf(
            arguments(Role.CUSTOMER),
            arguments(Role.SUPERVISOR),
            arguments(Role.WORKER)
        )

        @JvmStatic
        fun positiveCasesSourceForSetArg() = listOf(
            arguments(setOf(Role.WORKER)),
            arguments(setOf(Role.WORKER, Role.SUPERVISOR)),
            arguments(setOf(Role.WORKER, Role.SUPERVISOR, Role.CUSTOMER))
        )
    }
}