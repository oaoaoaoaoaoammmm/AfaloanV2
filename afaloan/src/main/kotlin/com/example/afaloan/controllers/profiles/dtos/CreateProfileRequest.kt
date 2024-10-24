package com.example.afaloan.controllers.profiles.dtos

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class CreateProfileRequest(
    @field:Size(min = 1, max = 20)
    val name: String,
    @field:Size(min = 1, max = 20)
    val surname: String,
    @field:Size(max = 20)
    val patronymic: String? = null,
    @Pattern(regexp = "^\\+7\\d{10}$")
    val phoneNumber: String,
    @field:Size(min = 4, max = 4)
    val passportSeries: String,
    @field:Size(min = 6, max = 6)
    val passportNumber: String,
    @field:Size(min = 14, max = 14)
    val snils: String? = null,
    @field:Pattern(regexp = "^\\d{3}-\\d{3}-\\d{3}-\\d{2}$")
    val inn: String? = null,
    @field:Min(1)
    val monthlyIncome: BigDecimal
)
