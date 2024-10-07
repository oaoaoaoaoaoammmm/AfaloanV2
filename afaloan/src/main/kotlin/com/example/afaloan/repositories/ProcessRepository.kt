package com.example.afaloan.repositories

import com.example.afaloan.models.Process
import com.example.afaloan.models.enumerations.ProcessStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface ProcessRepository: JpaRepository<Process, UUID> {

    @Query("""
        select process from Process process
        left join fetch process.bid
        where process.id = :id
    """)
    override fun findById(id: UUID): Optional<Process>

    @Query("""
        select process from Process process
        left join fetch process.bid bid
        left join fetch bid.microloan
        where process.status = :status
    """)
    fun findAllByStatus(status: ProcessStatus): List<Process>
}