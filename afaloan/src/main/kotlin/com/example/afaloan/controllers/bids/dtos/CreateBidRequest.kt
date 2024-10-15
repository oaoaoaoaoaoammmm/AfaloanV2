package com.example.afaloan.controllers.bids.dtos

import com.example.afaloan.models.enumerations.BidPriority
import jakarta.validation.constraints.Size
import java.util.*

data class CreateBidRequest(
    @field:Size(min = 1, max = 64)
    val target: String,
    @field:Size(max = 500)
    val coverLetter: String? = null,
    val priority: BidPriority,
    @field:Size(max = 64)
    val employeeMessage: String? = null,
    val profileId: UUID,
    val microloanId: UUID,
    val boilingPointId: UUID,
)
