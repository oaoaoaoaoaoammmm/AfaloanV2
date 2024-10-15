package com.example.afaloan.mappers

import com.example.afaloan.utils.createProfile
import com.example.afaloan.utils.createCreateProfileRequest
import com.example.afaloan.utils.createUpdateProfileRequest
import com.example.afaloan.utils.mockSecurityContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.core.context.SecurityContextHolder

class ProfileMapperTest {

    private val profileMapper = ProfileMapper()

    @BeforeEach
    fun setUp() = mockSecurityContext()

    @AfterEach
    fun tearDown() = SecurityContextHolder.clearContext()

    @Test
    fun `convert(request CreateProfileRequest) should execute successfully`() {
        val request = createCreateProfileRequest()

        val result = profileMapper.convert(request)

        assertThat(request.name).isEqualTo(result.name)
        assertThat(request.surname).isEqualTo(result.surname)
        assertThat(request.phoneNumber).isEqualTo(result.phoneNumber)
    }

    @Test
    fun `convert(request UpdateProfileRequest) should execute successfully`() {
        val request = createUpdateProfileRequest()

        val result = profileMapper.convert(request)

        assertThat(request.name).isEqualTo(result.name)
        assertThat(request.surname).isEqualTo(result.surname)
        assertThat(request.phoneNumber).isEqualTo(result.phoneNumber)
    }

    @Test
    fun `convert(profile Profile) should execute successfully`() {
        val profile = createProfile()

        val result = profileMapper.convert(profile)

        assertThat(result.name).isEqualTo(result.name)
        assertThat(result.surname).isEqualTo(result.surname)
        assertThat(result.phoneNumber).isEqualTo(result.phoneNumber)
    }
}