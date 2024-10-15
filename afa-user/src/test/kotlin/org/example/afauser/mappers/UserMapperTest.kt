package org.example.afauser.mappers

import org.assertj.core.api.Assertions.assertThat
import org.example.afauser.utils.USER
import org.junit.jupiter.api.Test

class UserMapperTest {

    private val userMapper = UserMapper()

    @Test
    fun `convert should execute successfully`() {

        val result = userMapper.convert(USER)

        assertThat(result.id).isNotNull()
        assertThat(result.username).isEqualTo(USER.username)
        assertThat(result.role).isEqualTo(USER.role)
    }
}