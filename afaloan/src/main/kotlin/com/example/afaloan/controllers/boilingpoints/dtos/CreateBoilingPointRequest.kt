package com.example.afaloan.controllers.boilingpoints.dtos

import jakarta.validation.constraints.Size

data class CreateBoilingPointRequest(
    @field:Size(min = 3, max = 32)
    val city: String,
    @field:Size(min = 3, max = 64)
    val address: String,
    @field:Size(min = 10, max = 100)
    val openingHours: String,
    @field:Size(max = 200)
    val info: String? = null
)
