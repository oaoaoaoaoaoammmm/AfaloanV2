package com.example.afaloan.mappers

import com.example.afaloan.services.BoilingPointService
import com.example.afaloan.services.MicroloanService
import com.example.afaloan.services.ProfileService
import com.example.afaloan.utils.createBid
import com.example.afaloan.utils.createCreateBidRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class BidMapperTest {

    private val profileService = mock<ProfileService>()
    private val microloanService = mock<MicroloanService>()
    private val boilingPointService = mock<BoilingPointService>()

    private val bidMapper = BidMapper(profileService, microloanService, boilingPointService)

    @Test
    fun `convertToDto should execute successfully`() {
        val bid = createBid()

        val result = bidMapper.convertToDto(bid)

        assertThat(bid.target).isEqualTo(result.target)
        assertThat(bid.status).isEqualTo(result.status)
        assertThat(bid.profile!!.id).isEqualTo(result.profileId)
        assertThat(bid.microloan!!.id).isEqualTo(result.microloanId)
        assertThat(bid.boilingPoint!!.id).isEqualTo(result.boilingPointId)
    }

    @Test
    fun `convertToView should execute successfully`() {
        val bid = createBid()

        val result = bidMapper.convertToView(bid)

        assertThat(bid.id).isEqualTo(result.id)
        assertThat(bid.target).isEqualTo(result.target)
        assertThat(bid.status).isEqualTo(result.status)
    }

    @Test
    fun `convert should execute successfully`() {
        val bid = createBid()
        val request = createCreateBidRequest(bid)
        whenever(profileService.find(any())).thenReturn(bid.profile)
        whenever(microloanService.find(any())).thenReturn(bid.microloan)
        whenever(boilingPointService.find(any())).thenReturn(bid.boilingPoint)

        val result = bidMapper.convert(request)

        assertThat(bid.target).isEqualTo(result.target)
        assertThat(bid.coverLetter).isEqualTo(result.coverLetter)
        assertThat(bid.status).isEqualTo(result.status)
        assertThat(bid.employeeMessage).isEqualTo(result.employeeMessage)
        assertThat(result.profile).isNotNull
        assertThat(result.microloan).isNotNull
        assertThat(result.boilingPoint).isNotNull
    }
}