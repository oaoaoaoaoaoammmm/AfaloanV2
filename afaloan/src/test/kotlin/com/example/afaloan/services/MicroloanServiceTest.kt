package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Microloan
import com.example.afaloan.repositories.MicroloanRepository
import com.example.afaloan.utils.createMicroloan
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
import java.math.BigDecimal
import java.util.*

class MicroloanServiceTest {

    private val microloanRepository = mock<MicroloanRepository>()

    private val microloanService = MicroloanService(microloanRepository)

    @Test
    fun `find should execute successfully`() {
        val microloan = createMicroloan()
        whenever(microloanRepository.findById(any())).thenReturn(Optional.of(microloan))

        val result = microloanService.find(microloan.id!!)

        assertThat(microloan.sum).isEqualTo(result.sum)
        assertThat(microloan.name).isEqualTo(result.name)
        assertThat(microloan.sum).isEqualTo(result.sum)
    }

    @Test
    fun `find should throw MICROLOAN_NOT_FOUND`() {
        whenever(microloanRepository.findById(any())).thenReturn(Optional.empty())

        val ex = assertThrows<InternalException> { microloanService.find(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.MICROLOAN_NOT_FOUND)
    }

    @Test
    fun `findAll should execute successfully`() {
        val microloans = listOf(createMicroloan(), createMicroloan(), createMicroloan())
        whenever(microloanRepository.findAll()).thenReturn(microloans)

        val result = microloanService.findAll()

        assertThat(microloans.size).isEqualTo(result.size)
    }

    @Test
    fun `findPage should execute successfully`() {
        val page = PageImpl(listOf(createMicroloan(), createMicroloan()))
        whenever(microloanRepository.findAll(any<Pageable>())).thenReturn(page)

        val result = microloanService.findPage(Pageable.ofSize(10))

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `create should execute successfully`() {
        val microloan = createMicroloan()
        whenever(microloanRepository.save(any<Microloan>())).thenReturn(microloan)

        val result = microloanService.create(microloan)

        assertThat(microloan.id!!).isEqualTo(result)
    }

    @Test
    fun `update should execute successfully`() {
        val oldMicroloan = createMicroloan()
        val newMicroloan = oldMicroloan.copy(name = "name ch", sum = BigDecimal.TWO)
        whenever(microloanRepository.findById(any())).thenReturn(Optional.of(oldMicroloan))
        whenever(microloanRepository.save(any<Microloan>())).thenReturn(newMicroloan)

        val result = microloanService.update(oldMicroloan.id!!, newMicroloan)

        assertThat(oldMicroloan.id).isEqualTo(result.id!!)
        assertThat(newMicroloan.name).isEqualTo(result.name)
        assertThat(newMicroloan.sum).isEqualTo(result.sum)
    }

    @Test
    fun `delete should execute successfully`() {
        assertDoesNotThrow { microloanService.delete(UUID.randomUUID()) }
    }
}