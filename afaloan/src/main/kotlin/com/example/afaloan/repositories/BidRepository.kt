package com.example.afaloan.repositories

import com.example.afaloan.models.Bid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BidRepository: JpaRepository<Bid, UUID> {

    @Query(value = """
        select bid from Bid bid
        left join fetch bid.profile
        left join fetch bid.microloan
        left join fetch bid.boilingPoint
        where bid.id = :id
    """)
    override fun findById(id: UUID): Optional<Bid>

    fun findPageByBoilingPointId(boilingPointId: UUID, pageable: Pageable): Page<Bid>

    fun findPageByProfileId(profileId: UUID, pageable: Pageable): Page<Bid>

    fun findPageByMicroloanId(microloanId: UUID, pageable: Pageable): Page<Bid>
}