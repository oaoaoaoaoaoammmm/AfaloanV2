package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Bid
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.repositories.BidRepository
import com.example.afaloan.utils.createBid
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import java.util.*

class BidServiceTest {

    private val bidRepository = mock<BidRepository>()

    private val bidService = BidService(bidRepository)

    @Test
    fun `find should execute successfully`() {
        val bid = createBid()
        whenever(bidRepository.findById(any())).thenReturn(Optional.of(bid))

        val result = bidService.find(bid.id!!)

        assertThat(bid.id).isEqualTo(result.id)
        assertThat(bid.target).isEqualTo(result.target)
        assertThat(bid.status).isEqualTo(bid.status)
    }

    @Test
    fun `find should throw BID_NOT_FOUND`() {
        whenever(bidRepository.findById(any())).thenReturn(Optional.empty())

        val ex = assertThrows<InternalException> { bidService.find(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.BID_NOT_FOUND)
    }

    @Test
    fun `findPageByBoilingPointId should execute successfully`() {
        val page = PageImpl(listOf( createBid(), createBid(), createBid()))
        whenever(bidRepository.findPageByBoilingPointId(any(), any())).thenReturn(page)

        val result = bidService.findPageByBoilingPointId(UUID.randomUUID(), Pageable.ofSize(10))

        assertThat(page.size).isEqualTo(result.size)
    }

    @Test
    fun `findPageByProfileId should execute successfully`() {
        val page = PageImpl(listOf( createBid(), createBid(), createBid()))
        whenever(bidRepository.findPageByProfileId(any(), any())).thenReturn(page)

        val result = bidService.findPageByProfileId(UUID.randomUUID(), Pageable.ofSize(10))

        assertThat(page.size).isEqualTo(result.size)
    }

    @Test
    fun `findPageByMicroloanId should execute successfully`() {
        val page = PageImpl(listOf( createBid(), createBid(), createBid()))
        whenever(bidRepository.findPageByMicroloanId(any(), any())).thenReturn(page)

        val result = bidService.findPageByMicroloanId(UUID.randomUUID(), Pageable.ofSize(10))

        assertThat(page.size).isEqualTo(result.size)
    }

    @Test
    fun `create should execute successfully`() {
        val bid = createBid()
        whenever(bidRepository.save(any<Bid>())).thenReturn(bid)

        val result = bidService.create(bid)

        assertThat(result).isNotNull()
    }

    @Test
    fun `updateStatus should execute successfully`() {
        val bid = createBid()
        whenever(bidRepository.findById(any())).thenReturn(Optional.of(bid))

        assertDoesNotThrow { bidService.updateStatus(bid.id!!, BidStatus.CLOSED) }
    }
}