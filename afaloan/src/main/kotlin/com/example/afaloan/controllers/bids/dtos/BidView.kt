package com.example.afaloan.controllers.bids.dtos

import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.BidStatus
import java.time.LocalDateTime
import java.util.*

data class BidView(
    val id: UUID? = null,
    val target: String,
    val date: LocalDateTime,
    val priority: BidPriority,
    val status: BidStatus,
)
