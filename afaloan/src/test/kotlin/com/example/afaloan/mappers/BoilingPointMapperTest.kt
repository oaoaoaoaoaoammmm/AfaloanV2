package com.example.afaloan.mappers

import com.example.afaloan.utils.createCreateBoilingPointRequest
import com.example.afaloan.utils.createUpdateBoilingPointRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BoilingPointMapperTest {

    private val boilingPointMapper = BoilingPointMapper()

    @Test
    fun `convert(request CreateBoilingPointRequest) should execute successfully`() {
        val request = createCreateBoilingPointRequest()

        val result = boilingPointMapper.convert(request)

        assertThat(result.city).isEqualTo(request.city)
        assertThat(result.address).isEqualTo(request.address)
        assertThat(result.openingHours).isEqualTo(request.openingHours)
    }

    @Test
    fun `convert(request UpdateBoilingPointRequest) should execute successfully`() {
        val request = createUpdateBoilingPointRequest()

        val result = boilingPointMapper.convert(request)

        assertThat(result.city).isEqualTo(request.city)
        assertThat(result.address).isEqualTo(request.address)
        assertThat(result.openingHours).isEqualTo(request.openingHours)
    }
}