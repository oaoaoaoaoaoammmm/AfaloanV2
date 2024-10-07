package com.example.afaloan.controller.boilingpoints.dtos

import jakarta.validation.constraints.Size

data class UpdateBoilingPointRequest(
    @field:Size(min = 3, max = 32)
    val city: String,
    @field:Size(min = 3, max = 64)
    val address: String,
    @field:Size(min = 10, max = 100)
    val openingHours: String,
    @field:Size(max = 200)
    val info: String? = null
)