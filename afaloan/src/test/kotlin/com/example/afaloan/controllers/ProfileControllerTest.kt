package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controllers.profiles.dtos.CreateProfileResponse
import com.example.afaloan.controllers.profiles.dtos.ProfileDto
import com.example.afaloan.utils.createCreateProfileRequest
import com.example.afaloan.utils.createUpdateProfileRequest
import com.example.afaloan.utils.toJson
import com.example.afaloan.utils.toObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class ProfileControllerTest : BaseIntegrationTest() {

    @Test
    fun `create should return CREATED`() {
        createProfile()
    }

    @Test
    fun `find should return OK`() {
        val profileId = createProfile()
        mockMvc.perform(
            get("/profiles/$profileId")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.name").isNotEmpty,
            jsonPath("$.surname").isNotEmpty
        )
    }

    @Test
    fun `update should return OK`() {
        val profileId = createProfile()
        val request = createUpdateProfileRequest()
        val updatedProfile = mockMvc.perform(
            put("/profiles/$profileId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request.toJson())
        ).andExpectAll(
            status().isOk,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.name").isNotEmpty,
            jsonPath("$.surname").isNotEmpty
        ).andReturn().response.contentAsString.toObject<ProfileDto>()

        assertThat(updatedProfile.name).isEqualTo(request.name)
        assertThat(updatedProfile.surname).isEqualTo(request.surname)
        assertThat(updatedProfile.patronymic).isEqualTo(request.patronymic)
        assertThat(updatedProfile.passportSeries).isEqualTo(request.passportSeries)
    }

    private fun createProfile(): UUID {
        val response = mockMvc.perform(
            post("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCreateProfileRequest().toJson())
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        ).andReturn().response.contentAsString.toObject<CreateProfileResponse>()
        return response.id
    }
}