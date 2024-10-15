package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controllers.bids.dtos.CreateBidRequest
import com.example.afaloan.controllers.bids.dtos.CreateBidResponse
import com.example.afaloan.controllers.boilingpoints.dtos.CreateBoilingPointResponse
import com.example.afaloan.controllers.microloans.dtos.CreateMicroloanResponse
import com.example.afaloan.controllers.processes.dtos.CreateProcessRequest
import com.example.afaloan.controllers.processes.dtos.CreateProcessResponse
import com.example.afaloan.controllers.processes.dtos.ProcessDto
import com.example.afaloan.controllers.profiles.dtos.CreateProfileResponse
import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.ProcessStatus
import com.example.afaloan.utils.toObject
import com.example.afaloan.utils.toJson
import com.example.afaloan.utils.createCreateProfileRequest
import com.example.afaloan.utils.createMicroloanDto
import com.example.afaloan.utils.createCreateBoilingPointRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.util.*

class ProcessControllerTest : BaseIntegrationTest() {

    @Test
    fun `create should return CREATED`() {
        createProcess()
    }

    @Test
    fun `find should return OK`() {
        val processId = createProcess()
        mockMvc.perform(
            get("/processes/$processId")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.debt").isNotEmpty,
            jsonPath("$.comment").isNotEmpty,
            jsonPath("$.status").isNotEmpty,
            jsonPath("$.bidId").isNotEmpty
        )
    }

    @Test
    fun `findPage should return OK`() {
        createProcess()
        mockMvc.perform(
            get("/processes")
                .param("page", "0")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.content[0].id").isNotEmpty,
            jsonPath("$.content[0].debt").isNotEmpty,
            jsonPath("$.content[0].comment").isNotEmpty,
            jsonPath("$.content[0].status").isNotEmpty
        )
    }

    @Test
    fun `update should return OK`() {
        val processId = createProcess()
        mockMvc.perform(
            put("/processes/$processId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    ProcessDto(
                        debt = BigDecimal.ZERO,
                        comment = "comment",
                        status = ProcessStatus.CLOSED,
                        bidId = createBid()
                    ).toJson()
                )
        ).andExpectAll(
            status().isOk,
            jsonPath("$.debt").value(BigDecimal.ZERO.toString()),
            jsonPath("$.comment").value("comment"),
            jsonPath("$.status").value(ProcessStatus.CLOSED.toString()),
            jsonPath("$.bidId").isNotEmpty
        )
    }

    private fun createProcess(): UUID {
        val response = mockMvc.perform(
            post("/processes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    CreateProcessRequest(
                        debt = BigDecimal.TEN,
                        comment = "comment",
                        bidId = createBid()
                    ).toJson()
                )
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        ).andReturn().response.contentAsString.toObject<CreateProcessResponse>()
        return response.id
    }

    private fun createBid(): UUID {
        val response = mockMvc.perform(
            post("/bids")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    CreateBidRequest(
                        target = "target",
                        coverLetter = "cover letter",
                        priority = BidPriority.MEDIUM,
                        employeeMessage = "employee message",
                        profileId = createProfile(),
                        microloanId = createMicroloan(),
                        boilingPointId = createBoilingPoint()
                    ).toJson()
                )
        ).andExpectAll(
            status().isCreated,
            jsonPath("$.id").isNotEmpty
        ).andReturn().response.contentAsString.toObject<CreateBidResponse>()
        return response.id
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