package com.example.afaloan.models

import com.example.afaloan.models.enumerations.ProcessStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "processes")
data class Process(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "debt")
    val debt: BigDecimal,

    @Column(name = "comment")
    val comment: String,

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    val status: ProcessStatus,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bid_id", referencedColumnName = "id")
    val bid: Bid? = null
)
