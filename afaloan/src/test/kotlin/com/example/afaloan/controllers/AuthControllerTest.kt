package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controller.auth.dtos.AuthorizeUserRequest
import com.example.afaloan.controller.auth.dtos.RegisterUserRequest
import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.utils.UNAUTHORIZED_USER
import com.example.afaloan.utils.USER
import com.example.afaloan.utils.USER_PASSWORD
import com.example.afaloan.utils.toJson
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AuthControllerTest : BaseIntegrationTest() {

    @Test
    fun `register should return CREATED`() {
        mockMvc.perform(
            post("$API_PREFIX/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    RegisterUserRequest(
                        username = UNAUTHORIZED_USER.username,
                        password = UNAUTHORIZED_USER.password,
                        roles = listOf(Role.WORKER)
                    ).toJson()
                )
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        )
    }

    @Test
    fun `authorize should return CREATED`() {
        mockMvc.perform(
            post("$API_PREFIX/auth/tokens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    AuthorizeUserRequest(
                        username = USER.username,
                        password = USER_PASSWORD
                    ).toJson()
                )
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.username").isNotEmpty,
            jsonPath("$.access").isNotEmpty,
            jsonPath("$.refresh").isNotEmpty
        )
    }

    @Test
    fun `reAuthorize should return BAD_REQUEST`() {
        mockMvc.perform(
            post("$API_PREFIX/auth/tokens/refresh")
                .param("refresh", "refresh")
        ).andExpectAll(
            status().isBadRequest,
            jsonPath("$.code").value(ErrorCode.TOKEN_INCORRECT_FORMAT.name)
        )
    }
}