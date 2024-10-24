package com.example.afaloan.mappers

import com.example.afaloan.controllers.processes.dtos.CreateProcessRequest
import com.example.afaloan.controllers.processes.dtos.ProcessDto
import com.example.afaloan.controllers.processes.dtos.ProcessView
import com.example.afaloan.models.Process
import com.example.afaloan.models.enumerations.ProcessStatus
import com.example.afaloan.services.BidService
import org.springframework.stereotype.Component

@Component
class ProcessMapper(
    private val bidService: BidService
) {

    fun convertToDto(process: Process): ProcessDto {
        return ProcessDto(
            debt = process.debt,
            status = process.status,
            comment = process.comment,
            bidId = process.bid!!.id!!
        )
    }

    fun convertToView(process: Process): ProcessView {
        return ProcessView(
            id = process.id!!,
            debt = process.debt,
            status = process.status,
            comment = process.comment,
        )
    }

    fun convert(request: CreateProcessRequest): Process {
        return Process(
            debt = request.debt,
            status = ProcessStatus.CREATED,
            comment = request.comment,
            bid = bidService.find(request.bidId)
        )
    }

    fun convert(dto: ProcessDto): Process {
        return Process(
            debt = dto.debt,
            comment = dto.comment,
            status = dto.status,
            bid = bidService.find(dto.bidId)
        )
    }
}