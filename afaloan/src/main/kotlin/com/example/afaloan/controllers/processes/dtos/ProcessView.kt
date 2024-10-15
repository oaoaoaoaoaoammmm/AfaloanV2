package com.example.afaloan.controllers.processes.dtos

import com.example.afaloan.models.enumerations.ProcessStatus
import java.math.BigDecimal
import java.util.*

data class ProcessView(
    val id: UUID,
    val debt: BigDecimal,
    val status: ProcessStatus,
    val comment: String
)
