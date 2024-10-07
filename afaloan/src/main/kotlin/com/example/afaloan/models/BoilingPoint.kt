package com.example.afaloan.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.*

@Entity
@Table(name = "boiling_points")
data class BoilingPoint(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "city")
    val city: String,

    @Column(name = "address")
    val address: String,

    @Column(name = "opening_hours")
    val openingHours: String,

    @Column(name = "info")
    val info: String? = null
)
