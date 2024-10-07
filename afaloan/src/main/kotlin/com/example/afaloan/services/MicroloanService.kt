package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Microloan
import com.example.afaloan.repositories.MicroloanRepository
import com.example.afaloan.utils.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class MicroloanService(
    private val microloanRepository: MicroloanRepository
) {

    fun find(id: UUID): Microloan {
        logger.info { "Finding microloan by id - $id" }
        return microloanRepository.findById(id).orElseThrow {
            InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.MICROLOAN_NOT_FOUND)
        }
    }

    fun findAll(): List<Microloan> {
        logger.info { "Finding all microloans" }
        return microloanRepository.findAll()
    }

    fun findPage(pageable: Pageable): Page<Microloan> {
        logger.info { "Finding microloan's page - № ${pageable.pageNumber}" }
        return microloanRepository.findAll(pageable)
    }

    fun create(microloan: Microloan): UUID {
        logger.info { "Creating microloan" }
        return microloanRepository.save(microloan).id!!
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun update(id: UUID, microloan: Microloan): Microloan {
        logger.info { "Updating microloan by id - $id" }
        val oldMicroloan = find(id)
        val newMicroloan = microloan.copy(id = oldMicroloan.id)
        return microloanRepository.save(newMicroloan)
    }

    fun delete(id: UUID) {
        logger.info { "Deleting microloan by id - $id" }
        microloanRepository.deleteById(id)
    }
}