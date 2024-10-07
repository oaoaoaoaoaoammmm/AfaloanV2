package com.example.afaloan.mappers

import com.example.afaloan.utils.createMicroloan
import com.example.afaloan.utils.createMicroloanDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class MicroloanMapperTest {

    private val microloanMapper = MicroloanMapper()

    @Test
    fun `convert(microloan Microloan) should execute successfully`() {
        val microloan = createMicroloan()

        val result = microloanMapper.convert(microloan)

        assertThat(microloan.name).isEqualTo(result.name)
        assertThat(microloan.sum).isEqualTo(result.sum)
        assertThat(microloan.monthlyIncomeRequirement).isEqualTo(result.monthlyIncomeRequirement)
    }

    @Test
    fun `convert(dto MicroloanDto) should execute successfully`() {
        val dto = createMicroloanDto()

        val result = microloanMapper.convert(dto)

        assertThat(dto.name).isEqualTo(result.name)
        assertThat(dto.sum).isEqualTo(result.sum)
        assertThat(dto.monthlyIncomeRequirement).isEqualTo(result.monthlyIncomeRequirement)
    }
}