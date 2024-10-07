package com.example.afaloan.mappers

import com.example.afaloan.models.UserRole
import com.example.afaloan.utils.USER
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserMapperTest {

    private val userMapper = UserMapper()

    @Test
    fun `convert should execute successfully`() {

        val result = userMapper.convert(USER)

        assertThat(result.id).isNotNull()
        assertThat(result.username).isEqualTo(USER.username)
        assertThat(result.roles).isEqualTo(USER.roles.map(UserRole::role))
    }
}