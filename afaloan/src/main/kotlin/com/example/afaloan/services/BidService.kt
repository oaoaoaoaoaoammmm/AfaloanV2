package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Bid
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.repositories.BidRepository
import com.example.afaloan.utils.logger
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class BidService(
    private val bidRepository: BidRepository
) {

    fun find(id: UUID): Bid {
        logger.info { "Finding bid by id - $id" }
        return bidRepository.findById(id).orElseThrow {
            InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.BID_NOT_FOUND)
        }
    }

    fun findPageByBoilingPointId(boilingPointId: UUID, pageable: Pageable): Page<Bid> {
        logger.info { "Finding page with number - ${pageable.pageNumber} by boiling point id - $boilingPointId" }
        return bidRepository.findPageByBoilingPointId(boilingPointId, pageable)
    }

    fun findPageByProfileId(profileId: UUID, pageable: Pageable): Page<Bid> {
        logger.info { "Finding page with number - ${pageable.pageNumber} by profile id - $profileId" }
        val result = bidRepository.findPageByProfileId(profileId, pageable)
        return result
    }

    fun findPageByMicroloanId(microloanID: UUID, pageable: Pageable): Page<Bid> {
        logger.info { "Finding page with number - ${pageable.pageNumber} by microloan id - $microloanID" }
        return bidRepository.findPageByMicroloanId(microloanID, pageable)
    }

    fun create(bid: Bid): UUID {
        logger.info { "Creating bid" }
        return bidRepository.save(bid).id!!
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateStatus(id: UUID, status: BidStatus, employeeMessage: String? = null) {
        logger.info { "Updating status - $status for bid with id - $id" }
        val bid = find(id)
        val closedBid = bid.copy(status = status, employeeMessage = employeeMessage)
        bidRepository.save(closedBid)
    }
}