package com.example.afaloan.mappers

import com.example.afaloan.services.BidService
import com.example.afaloan.utils.createBid
import com.example.afaloan.utils.createCreateProcessRequest
import com.example.afaloan.utils.createProcess
import com.example.afaloan.utils.createProcessDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ProcessMapperTest {

    private val bidService = mock<BidService>()

    private val processMapper = ProcessMapper(bidService)

    @Test
    fun `convertToDto should execute successfully`() {
        val process = createProcess()

        val result = processMapper.convertToDto(process)

        assertThat(process.debt).isEqualTo(result.debt)
        assertThat(process.comment).isEqualTo(result.comment)
        assertThat(process.status).isEqualTo(result.status)
    }

    @Test
    fun `convertToView should execute successfully`() {
        val process = createProcess()

        val result = processMapper.convertToView(process)

        assertThat(process.debt).isEqualTo(result.debt)
        assertThat(process.comment).isEqualTo(result.comment)
        assertThat(process.status).isEqualTo(result.status)
    }

    @Test
    fun `convert(request CreateProcessRequest) should execute successfully`() {
        val bid = createBid()
        val request = createCreateProcessRequest()
        whenever(bidService.find(any())).thenReturn(bid)

        val result = processMapper.convert(request)

        assertThat(request.debt).isEqualTo(result.debt)
        assertThat(request.comment).isEqualTo(result.comment)
    }

    @Test
    fun `convert(dto ProcessDto) should execute successfully`() {
        val bid = createBid()
        val dto = createProcessDto()
        whenever(bidService.find(any())).thenReturn(bid)

        val result = processMapper.convert(dto)

        assertThat(dto.debt).isEqualTo(result.debt)
        assertThat(dto.comment).isEqualTo(result.comment)
        assertThat(dto.status).isEqualTo(result.status)
    }
}