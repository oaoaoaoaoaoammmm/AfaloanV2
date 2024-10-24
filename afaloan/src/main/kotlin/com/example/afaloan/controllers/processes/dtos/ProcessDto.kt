package com.example.afaloan.controllers.processes.dtos

import com.example.afaloan.models.enumerations.ProcessStatus
import java.math.BigDecimal
import java.util.*

data class ProcessDto(
    val debt: BigDecimal,
    val comment: String,
    val status: ProcessStatus,
    val bidId: UUID
)
