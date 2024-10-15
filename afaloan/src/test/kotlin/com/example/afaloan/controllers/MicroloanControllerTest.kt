package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controllers.microloans.dtos.CreateMicroloanResponse
import com.example.afaloan.utils.createMicroloanDto
import com.example.afaloan.utils.toJson
import com.example.afaloan.utils.toObject
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

class MicroloanControllerTest : BaseIntegrationTest() {

    @Test
    fun `create should return CREATED`() {
        createMicroloan()
    }

    @Test
    fun `find should return OK`() {
        val microloanId = createMicroloan()
        mockMvc.perform(
            get("/microloans/$microloanId")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.name").isNotEmpty
        )
    }

    @Test
    fun `update should return OK`() {
        val microloanId = createMicroloan()
        mockMvc.perform(
            put("/microloans/$microloanId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    createMicroloanDto()
                        .copy(name = "name ch", otherRequirements = "other requirements ch")
                        .toJson()
                )
        ).andExpectAll(
            status().isOk,
            jsonPath("$.name").value("name ch"),
            jsonPath("$.otherRequirements").value("other requirements ch")
        )
    }

    @Test
    fun `delete should return NO_CONTENT`() {
        val microloanId = createMicroloan()
        mockMvc.perform(
            delete("/microloans/$microloanId")
        ).andExpect(status().isNoContent)
    }

    private fun createMicroloan(): UUID {
        val response = mockMvc.perform(
            post("/microloans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createMicroloanDto().toJson())
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        ).andReturn().response.contentAsString.toObject<CreateMicroloanResponse>()
        return response.id
    }
}