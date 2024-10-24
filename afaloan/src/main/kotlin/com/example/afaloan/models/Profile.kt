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
@Table(name = "profiles")
data class Profile(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,

    @Column(name = "name")
    val name: String,

    @Column(name = "surname")
    val surname: String,

    @Column(name = "patronymic")
    val patronymic: String? = null,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "passport_series")
    val passportSeries: String,

    @Column(name = "passport_number")
    val passportNumber: String,

    @Column(name = "snils")
    val snils: String? = null,

    @Column(name = "inn")
    val inn: String? = null,

    @Column(name = "monthly_income")
    val monthlyIncome: BigDecimal,

    @Column(name = "user_id")
    val userId: UUID
)
