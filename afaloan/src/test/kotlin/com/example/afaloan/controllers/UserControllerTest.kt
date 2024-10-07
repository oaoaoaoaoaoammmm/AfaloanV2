package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controller.users.dtos.UpdateRolesRequest
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.utils.USER
import com.example.afaloan.utils.toJson
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class UserControllerTest : BaseIntegrationTest() {

    @Test
    fun `find should return OK`() {
        mockMvc.perform(
            get("$API_PREFIX/users/${USER.id}")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.username").isNotEmpty
        )
    }

    @Test
    fun `delete should return NO_CONTENT`() {
        mockMvc.perform(
            delete("$API_PREFIX/users/${USER.id}")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `updateRoles should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$API_PREFIX/users/${USER.id}/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    UpdateRolesRequest(listOf(Role.WORKER)).toJson()
                )
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `block should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$API_PREFIX/users/${USER.id}/block")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `unblock should return NO_CONTENT`() {
        mockMvc.perform(
            delete("$API_PREFIX/users/${USER.id}/block")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `confirm should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$API_PREFIX/users/${USER.id}/confirm")
        ).andExpect(status().isNoContent)
    }
}