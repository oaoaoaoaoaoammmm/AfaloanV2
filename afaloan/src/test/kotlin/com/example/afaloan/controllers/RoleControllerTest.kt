package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class RoleControllerTest: BaseIntegrationTest() {

    @Test
    fun `find all roles should return OK`() {
        mockMvc.perform(
            get("$API_PREFIX/roles")
        ).andExpectAll(
            status().isOk,
            jsonPath("$").isNotEmpty
        )
    }
}