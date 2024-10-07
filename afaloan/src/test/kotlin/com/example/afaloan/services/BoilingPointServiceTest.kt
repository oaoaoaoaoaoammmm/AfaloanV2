package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.BoilingPoint
import com.example.afaloan.repositories.BoilingPointRepository
import com.example.afaloan.utils.createBoilingPoint
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

class BoilingPointServiceTest {

    private val boilingPointRepository = mock<BoilingPointRepository>()

    private val boilingPointService = BoilingPointService(boilingPointRepository)

    @Test
    fun `find(id UUID) should execute successfully`() {
        val boilingPoint = createBoilingPoint()
        whenever(boilingPointRepository.findById(any())).thenReturn(Optional.of(boilingPoint))

        val result = boilingPointService.find(boilingPoint.id!!)

        assertThat(result.id).isEqualTo(boilingPoint.id)
        assertThat(result.city).isEqualTo(boilingPoint.city)
        assertThat(result.address).isEqualTo(boilingPoint.address)
    }

    @Test
    fun `find(id UUID) should throw BOILING_POINT_NOT_FOUND`() {
        whenever(boilingPointRepository.findById(any())).thenReturn(Optional.empty())

        val ex = assertThrows<InternalException> { boilingPointService.find(UUID.randomUUID()) }

        assertThat(ex.httpStatus).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(ex.errorCode).isEqualTo(ErrorCode.BOILING_POINT_NOT_FOUND)
    }

    @Test
    fun `findPage should execute successfully`() {
        val page = PageImpl(listOf(createBoilingPoint(), createBoilingPoint()))
        whenever(boilingPointRepository.findAll(any<Pageable>())).thenReturn(page)

        val result = boilingPointService.findPage(Pageable.ofSize(10))

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `create should execute successfully`() {
        val boilingPoint = createBoilingPoint()
        whenever(boilingPointRepository.save(any<BoilingPoint>())).thenReturn(boilingPoint)

        val result = boilingPointService.create(boilingPoint)

        assertThat(result).isNotNull()
    }

    @Test
    fun `update should execute successfully`() {
        val oldBoilingPoint = createBoilingPoint()
        val newBoilingPoint = createBoilingPoint().copy(city = "ch city", address = "ch address")
        whenever(boilingPointRepository.findById(any())).thenReturn(Optional.of(oldBoilingPoint))
        whenever(boilingPointRepository.save(any<BoilingPoint>())).thenReturn(newBoilingPoint)

        val result = boilingPointService.update(oldBoilingPoint.id!!, newBoilingPoint)

        assertThat(result.id).isEqualTo(newBoilingPoint.id)
        assertThat(result.city).isEqualTo(newBoilingPoint.city)
        assertThat(result.address).isEqualTo(newBoilingPoint.address)
    }

    @Test
    fun `delete should execute successfully`() {
        assertDoesNotThrow { boilingPointService.delete(UUID.randomUUID()) }
    }
}