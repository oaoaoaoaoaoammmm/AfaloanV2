package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Profile
import com.example.afaloan.repositories.ProfileRepository
import com.example.afaloan.utils.createProfile
import com.example.afaloan.utils.mockSecurityContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

class ProfileServiceTest {

    private val profileRepository = mock<ProfileRepository>()

    private val profileService = ProfileService(profileRepository)

    @BeforeEach
    fun setUp() = mockSecurityContext()

    @AfterEach
    fun tearDown() = SecurityContextHolder.clearContext()

    @Test
    fun `find(id UUID) should execute successfully`() {
        val profile = createProfile()
        whenever(profileRepository.findById(any())).thenReturn(Optional.of(profile))

        val result = profileService.find(profile.id!!)

        assertThat(result.id).isEqualTo(profile.id)
        assertThat(result.name).isEqualTo(profile.name)
        assertThat(result.phoneNumber).isEqualTo(profile.phoneNumber)
    }

    @Test
    fun `find(id UUID) should throw NOT_FOUND`() {
        whenever(profileRepository.findByIdAndUserId(any(), any())).thenReturn(null)

        val ex = assertThrows<InternalException> { profileService.find(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.PROFILE_NOT_FOUND)
    }

    @Test
    fun `find(id UUID, userId UUID) should execute successfully`() {
        val profile = createProfile()
        whenever(profileRepository.findByIdAndUserId(any(), any())).thenReturn(profile)

        val result = profileService.find(profile.id!!, profile.userId)

        assertThat(result.id).isEqualTo(profile.id)
        assertThat(result.name).isEqualTo(profile.name)
        assertThat(result.phoneNumber).isEqualTo(profile.phoneNumber)
    }

    @Test
    fun `find(id UUID, userId UUID) should throw NOT_FOUND`() {
        whenever(profileRepository.findByIdAndUserId(any(), any())).thenReturn(null)

        val ex = assertThrows<InternalException> { profileService.find(UUID.randomUUID(), UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.PROFILE_NOT_FOUND)
    }

    @Test
    fun `findByUserId should execute successfully`() {
        val profile = createProfile()
        whenever(profileRepository.findByUserId(any())).thenReturn(profile)

        val result = profileService.findByUserId(profile.userId)

        assertThat(result.id).isEqualTo(profile.id)
        assertThat(result.name).isEqualTo(profile.name)
        assertThat(result.phoneNumber).isEqualTo(profile.phoneNumber)
    }

    @Test
    fun `findByUserId should throw NOT_FOUND`() {
        whenever(profileRepository.findByUserId(any())).thenReturn(null)

        val ex = assertThrows<InternalException> { profileService.findByUserId(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.PROFILE_NOT_FOUND)
    }

    @Test
    fun `create should execute successfully`() {
        val profile = createProfile()
        whenever(profileRepository.save(any<Profile>())).thenReturn(profile)

        val result = profileService.create(profile)

        assertThat(result).isEqualTo(profile.id)
    }

    @Test
    fun `update should execute successfully`() {
        val oldProfile = createProfile()
        val newProfile = oldProfile.copy(name = "ch name", surname = "ch surname")
        whenever(profileRepository.findByIdAndUserId(any(), any())).thenReturn(oldProfile)
        whenever(profileRepository.save(any<Profile>())).thenReturn(newProfile)

        val result = profileService.update(oldProfile.id!!, oldProfile)

        assertThat(result.id).isEqualTo(newProfile.id)
        assertThat(result.name).isEqualTo(newProfile.name)
        assertThat(result.phoneNumber).isEqualTo(newProfile.phoneNumber)
    }
}