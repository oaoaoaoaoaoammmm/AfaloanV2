package com.example.afaloan.controllers.bids.dtos

import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.BidStatus
import java.time.LocalDateTime
import java.util.UUID

data class BidDto(
    val target: String,
    val coverLetter: String? = null,
    val date: LocalDateTime,
    val priority: BidPriority,
    val status: BidStatus,
    val employeeMessage: String? = null,
    val profileId: UUID,
    val microloanId: UUID,
    val boilingPointId: UUID,
)
