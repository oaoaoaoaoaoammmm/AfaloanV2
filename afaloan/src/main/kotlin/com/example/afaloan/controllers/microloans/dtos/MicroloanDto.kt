package com.example.afaloan.controllers.microloans.dtos

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class MicroloanDto(
    @field:Size(min = 1, max = 32)
    val name: String,
    @field:Min(value = 1)
    val sum: BigDecimal,
    @field:Min(value = 0)
    val monthlyInterest: BigDecimal,
    @field:Size(min = 1, max = 200)
    val conditions: String,
    @field:Min(value = 1)
    val monthlyIncomeRequirement: BigDecimal,
    @field:Size(max = 200)
    val otherRequirements: String? = null
)
