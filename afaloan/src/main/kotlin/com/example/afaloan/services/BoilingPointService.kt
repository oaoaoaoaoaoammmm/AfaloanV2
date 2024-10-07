package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.BoilingPoint
import com.example.afaloan.repositories.BoilingPointRepository
import com.example.afaloan.utils.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BoilingPointService(
    private val boilingPointRepository: BoilingPointRepository
) {

    fun find(id: UUID): BoilingPoint {
        logger.info { "Finding boiling point by id - $id" }
        return boilingPointRepository.findById(id).orElseThrow{
            InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.BOILING_POINT_NOT_FOUND)
        }
    }

    fun findPage(pageable: Pageable): Page<BoilingPoint> {
        logger.info { "Finding boiling point's page - № ${pageable.pageNumber}" }
        return boilingPointRepository.findAll(pageable)
    }

    fun create(boilingPoint: BoilingPoint): UUID {
        logger.info { "Creating boiling point" }
        return boilingPointRepository.save(boilingPoint).id!!
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun update(id: UUID, boilingPoint: BoilingPoint): BoilingPoint {
        logger.info { "Updating boiling point by id - $id" }
        val oldBoilingPoint = find(id)
        val newBoilingPoint = boilingPoint.copy(id = oldBoilingPoint.id)
        return boilingPointRepository.save(newBoilingPoint)
    }

    fun delete(id: UUID) {
        logger.info { "Deleting boiling point by id - $id" }
        boilingPointRepository.deleteById(id)
    }
}