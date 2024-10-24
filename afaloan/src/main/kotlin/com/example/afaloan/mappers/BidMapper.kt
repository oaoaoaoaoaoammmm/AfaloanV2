package com.example.afaloan.mappers

import com.example.afaloan.controllers.bids.dtos.BidDto
import com.example.afaloan.controllers.bids.dtos.BidView
import com.example.afaloan.controllers.bids.dtos.CreateBidRequest
import com.example.afaloan.models.Bid
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.services.BoilingPointService
import com.example.afaloan.services.MicroloanService
import com.example.afaloan.services.ProfileService
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class BidMapper(
    private val profileService: ProfileService,
    private val microloanService: MicroloanService,
    private val boilingPointService: BoilingPointService
) {

    fun convertToDto(bid: Bid): BidDto {
        return BidDto(
            target = bid.target,
            coverLetter = bid.coverLetter,
            date = bid.date,
            priority = bid.priority,
            status = bid.status,
            employeeMessage = bid.employeeMessage,
            profileId = bid.profile!!.id!!,
            microloanId = bid.microloan!!.id!!,
            boilingPointId = bid.boilingPoint!!.id!!
        )
    }

    fun convertToView(bid: Bid): BidView {
        return BidView(
            id = bid.id,
            target = bid.target,
            date = bid.date,
            priority = bid.priority,
            status = bid.status
        )
    }

    fun convert(request: CreateBidRequest): Bid {
        return Bid(
            id = null,
            target = request.target,
            coverLetter = request.coverLetter,
            date = LocalDateTime.now(),
            priority = request.priority,
            status = BidStatus.UNDER_CONSIDERATION,
            employeeMessage = request.employeeMessage,
            profile = profileService.find(request.profileId),
            microloan = microloanService.find(request.microloanId),
            boilingPoint = boilingPointService.find(request.boilingPointId)
        )
    }
}