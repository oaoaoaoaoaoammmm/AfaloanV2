package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Process
import com.example.afaloan.models.enumerations.ProcessStatus
import com.example.afaloan.repositories.ProcessRepository
import com.example.afaloan.utils.logger
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.*

@Service
class ProcessService(
    private val processRepository: ProcessRepository
) {

    fun find(id: UUID): Process {
        logger.info { "Finding process by id - $id" }
        return processRepository.findById(id).orElseThrow {
            InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.PROCESS_NOT_FOUND)
        }
    }

    fun findPage(pageable: Pageable): Page<Process> {
        logger.info { "Finding process by page № - ${pageable.pageNumber}" }
        return processRepository.findAll(pageable)
    }

    fun create(process: Process): UUID {
        logger.info { "Creating process" }
        return processRepository.save(process).id!!
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun update(id: UUID, process: Process): Process {
        logger.info { "Updating process by id - $id" }
        val oldProcess = find(id)
        val newProcess = process.copy(id = oldProcess.id)
        return processRepository.save(newProcess)
    }

    /**
     * Calculates interest on the process with status ProcessStatus.IN_PROCESSING once a month
     *
     * @author Daniil Afanasev
     */
    @Transactional
    @Scheduled(cron = "\${api.job.scheduler.process-debt}")
    @SchedulerLock(name = "calculateMonthlyInterest")
    fun calculateMonthlyInterest() {
        val processes = processRepository.findAllByStatus(ProcessStatus.IN_PROCESSING)
        val calculateProcess =
            processes.map {
                it.copy(
                    debt = it.debt.multiply(BigDecimal.ONE.plus(it.bid!!.microloan!!.monthlyInterest))
                )
            }
        processRepository.saveAll(calculateProcess)
    }
}