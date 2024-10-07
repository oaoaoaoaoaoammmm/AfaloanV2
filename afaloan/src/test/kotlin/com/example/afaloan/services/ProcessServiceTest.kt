package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Process
import com.example.afaloan.models.enumerations.ProcessStatus
import com.example.afaloan.repositories.ProcessRepository
import com.example.afaloan.utils.createProcess
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

class ProcessServiceTest {

    private val processRepository = mock<ProcessRepository>()

    private val processService = ProcessService(processRepository)

    @Test
    fun `find should execute successfully`() {
        val process = createProcess()
        whenever(processRepository.findById(any())).thenReturn(Optional.of(process))

        val result = processService.find(process.id!!)

        assertThat(process.debt).isEqualTo(result.debt)
        assertThat(process.comment).isEqualTo(result.comment)
        assertThat(process.status).isEqualTo(result.status)
    }

    @Test
    fun `find should throw PROCESS_NOT_FOUND`() {
        whenever(processRepository.findById(any())).thenReturn(Optional.empty())

        val ex = assertThrows<InternalException> { processService.find(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.PROCESS_NOT_FOUND)
    }

    @Test
    fun `findPage should execute successfully`() {
        val processes = listOf(createProcess(), createProcess(), createProcess())
        whenever(processRepository.findAll(any<Pageable>())).thenReturn(PageImpl(processes))

        val result = processService.findPage(Pageable.ofSize(10))

        assertThat(processes.size).isEqualTo(result.size)
    }

    @Test
    fun `create should execute successfully`() {
        val process = createProcess()
        whenever(processRepository.save(any<Process>())).thenReturn(process)

        val result = processService.create(process)

        assertThat(result).isNotNull()
    }

    @Test
    fun `update should execute successfully`() {
        val oldProcess = createProcess()
        val newProcess = oldProcess.copy(debt = BigDecimal.ZERO, status = ProcessStatus.CLOSED)
        whenever(processRepository.findById(any())).thenReturn(Optional.of(oldProcess))
        whenever(processRepository.save(any<Process>())).thenReturn(newProcess)

        val result = processService.update(oldProcess.id!!, newProcess)

        assertThat(newProcess.debt).isEqualTo(result.debt)
        assertThat(newProcess.comment).isEqualTo(result.comment)
        assertThat(newProcess.status).isEqualTo(result.status)
    }

    @Test
    fun `calculateMonthlyInterest should execute successfully`() {
        val processes = listOf(createProcess(), createProcess(), createProcess())
        whenever(processRepository.findAllByStatus(any())).thenReturn(processes)

        assertDoesNotThrow { processService.calculateMonthlyInterest() }
    }
}