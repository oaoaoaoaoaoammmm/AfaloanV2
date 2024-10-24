package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controllers.bids.dtos.BidDto
import com.example.afaloan.controllers.bids.dtos.CreateBidRequest
import com.example.afaloan.controllers.bids.dtos.CreateBidResponse
import com.example.afaloan.controllers.boilingpoints.dtos.CreateBoilingPointResponse
import com.example.afaloan.controllers.microloans.dtos.CreateMicroloanResponse
import com.example.afaloan.controllers.profiles.dtos.CreateProfileResponse
import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.utils.toObject
import com.example.afaloan.utils.toJson
import com.example.afaloan.utils.createCreateProfileRequest
import com.example.afaloan.utils.createMicroloanDto
import com.example.afaloan.utils.createCreateBoilingPointRequest
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.UUID

class BidControllerTest: BaseIntegrationTest() {

    @Test
    fun `create should return CREATED`() {
        createBid()
    }

    @Test
    fun `find should return OK`() {
        createBidAndGet()
    }

    @Test
    fun `findPageByBoilingPointId should return OK`() {
        val bid = createBidAndGet()
        mockMvc.perform(
            get("/bids")
                .param("page", "0")
                .param("boilingPointId", bid.boilingPointId.toString())
        ).andExpectAll(
            status().isOk,
            jsonPath("$.content[0].id").isNotEmpty,
            jsonPath("$.content[0].target").value(bid.target),
            jsonPath("$.content[0].status").value(bid.status.name)
        )
    }

    @Test
    fun `findPageByProfileId should return OK`() {
        val bid = createBidAndGet()
        mockMvc.perform(
            get("/bids")
                .param("page", "0")
                .param("profileId", bid.profileId.toString())
        ).andExpectAll(
            status().isOk,
            jsonPath("$.content[0].id").isNotEmpty,
            jsonPath("$.content[0].target").value(bid.target),
            jsonPath("$.content[0].status").value(bid.status.name)
        )
    }

    @Test
    fun `findPageByMicroloanId should return OK`() {
        val bid = createBidAndGet()
        mockMvc.perform(
            get("/bids")
                .param("page", "0")
                .param("microloanId", bid.microloanId.toString())
        ).andExpectAll(
            status().isOk,
            jsonPath("$.content[0].id").isNotEmpty,
            jsonPath("$.content[0].target").value(bid.target),
            jsonPath("$.content[0].status").value(bid.status.name)
        )
    }

    @Test
    fun `updateStatus should return NO_CONTENT`() {
        val bidId = createBid()
        mockMvc.perform(
            patch("/bids/$bidId")
                .param("status", BidStatus.REJECTED.name)
                .param("employeeMessage", "не не не... не не не.")
        ).andExpect(status().isNoContent)
    }

    private fun createBidAndGet(): BidDto {
        val bidId = createBid()
        return mockMvc.perform(
            get("/bids/$bidId")
        ).andExpectAll(
            status().isOk,
            jsonPath("$.target").isNotEmpty,
            jsonPath("$.status").isNotEmpty,
            jsonPath("$.profileId").isNotEmpty,
        ).andReturn().response.contentAsString.toObject<BidDto>()
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