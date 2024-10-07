package com.example.afaloan.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal
import java.util.UUID

@Entity
@Table(name = "microloans")
data class Microloan(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "sum")
    val sum: BigDecimal,

    @Column(name = "monthly_interest")
    val monthlyInterest: BigDecimal,

    @Column(name = "conditions")
    val conditions: String,

    @Column(name = "monthly_income_requirement")
    val monthlyIncomeRequirement: BigDecimal,

    @Column(name = "other_requirements")
    val otherRequirements: String? = null
)