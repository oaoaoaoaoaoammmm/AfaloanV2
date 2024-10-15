package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controllers.boilingpoints.dtos.CreateBoilingPointResponse
import com.example.afaloan.utils.createCreateBoilingPointRequest
import com.example.afaloan.utils.createUpdateBoilingPointRequest
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

class BoilingPointControllerTest : BaseIntegrationTest() {

    @Test
    fun `create should return CREATED`() {
        createBoilingPoint()
    }

    @Test
    fun `find should return OK`() {
        val boilingPointId = createBoilingPoint()
        mockMvc.perform(
            get("/boiling-points/$boilingPointId")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.city").isNotEmpty,
            jsonPath("$.openingHours").isNotEmpty
        )
    }

    @Test
    fun `update should return OK`() {
        val boilingPointId = createBoilingPoint()
        mockMvc.perform(
            put("/boiling-points/$boilingPointId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createUpdateBoilingPointRequest().toJson())
        ).andExpectAll(
            status().isOk,
            jsonPath("$.id").isNotEmpty,
            jsonPath("$.city").isNotEmpty,
            jsonPath("$.openingHours").isNotEmpty
        )
    }

    @Test
    fun `delete should return OK`() {
        val boilingPointId = createBoilingPoint()
        mockMvc.perform(
            delete("/boiling-points/$boilingPointId")
        ).andExpect(status().isNoContent)
    }

    private fun createBoilingPoint(): UUID {
        val response = mockMvc.perform(
            post("/boiling-points")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createCreateBoilingPointRequest().toJson())
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        ).andReturn().response.contentAsString.toObject<CreateBoilingPointResponse>()
        return response.id
    }
}