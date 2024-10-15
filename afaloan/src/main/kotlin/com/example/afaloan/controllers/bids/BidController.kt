package com.example.afaloan.controllers.bids

import com.example.afaloan.controllers.bids.BidController.Companion.ROOT_URI
import com.example.afaloan.controllers.bids.dtos.BidDto
import com.example.afaloan.controllers.bids.dtos.BidView
import com.example.afaloan.controllers.bids.dtos.CreateBidRequest
import com.example.afaloan.controllers.bids.dtos.CreateBidResponse
import com.example.afaloan.mappers.BidMapper
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.services.BidService
import com.example.afaloan.utils.logger
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(ROOT_URI)
class BidController(
    private val bidMapper: BidMapper,
    private val bidService: BidService
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): BidDto {
        logger.trace { "Find bid by id - $id" }
        return bidService.find(id).let(bidMapper::convertToDto)
    }

    @GetMapping(params = ["boilingPointId"])
    @ResponseStatus(HttpStatus.OK)
    fun findPageByBoilingPointId(
        @RequestParam(required = false) boilingPointId: UUID,
        @Schema(hidden = true) @PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable
    ): Page<BidView> {
        logger.trace { "Find page of bids by boiling point id - $boilingPointId" }
        return bidService.findPageByBoilingPointId(boilingPointId, pageable).map(bidMapper::convertToView)
    }

    @GetMapping(params = ["profileId"])
    @ResponseStatus(HttpStatus.OK)
    fun findPageByProfileId(
        @RequestParam(required = false) profileId: UUID,
        @Schema(hidden = true) @PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable
    ): Page<BidView> {
        logger.trace { "Find page of bids by profile id - $profileId" }
        return bidService.findPageByProfileId(profileId, pageable).map(bidMapper::convertToView)
    }

    @GetMapping(params = ["microloanId"])
    @ResponseStatus(HttpStatus.OK)
    fun findPageByMicroloanId(
        @RequestParam(required = false) microloanId: UUID,
        @Schema(hidden = true) @PageableDefault(size = DEFAULT_PAGE_SIZE) pageable: Pageable
    ): Page<BidView> {
        logger.trace { "Find page of bids by microloan id - $microloanId" }
        return bidService.findPageByMicroloanId(microloanId, pageable).map(bidMapper::convertToView)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid request: CreateBidRequest): CreateBidResponse {
        logger.trace { "Create bid" }
        val bid = bidMapper.convert(request)
        val id = bidService.create(bid)
        return CreateBidResponse(id)
    }

    @PatchMapping(value = ["/{id}"], params = ["status"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyAuthority('SUPERVISOR', 'WORKER')")
    fun updateStatus(
        @PathVariable id: UUID,
        @RequestParam status: BidStatus,
        @RequestParam(required = false) employeeMessage: String? = null
    ) {
        logger.trace { "Update status - $status for bid with id - $id" }
        bidService.updateStatus(id, status, employeeMessage)
    }

    companion object {
        const val ROOT_URI = "/bids"
        const val DEFAULT_PAGE_SIZE = 50
    }
}