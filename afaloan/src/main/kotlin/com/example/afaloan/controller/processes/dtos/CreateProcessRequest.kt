package com.example.afaloan.controller.processes.dtos

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.util.UUID

data class CreateProcessRequest(
    @field:Min(1)
    val debt: BigDecimal,
    @field:Size(min = 1, max = 64)
    val comment: String,
    val bidId: UUID
)
