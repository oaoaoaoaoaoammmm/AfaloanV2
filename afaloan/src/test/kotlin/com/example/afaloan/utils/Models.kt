package com.example.afaloan.utils

import com.example.afaloan.controllers.bids.dtos.CreateBidRequest
import com.example.afaloan.controllers.boilingpoints.dtos.CreateBoilingPointRequest
import com.example.afaloan.controllers.boilingpoints.dtos.UpdateBoilingPointRequest
import com.example.afaloan.controllers.microloans.dtos.MicroloanDto
import com.example.afaloan.controllers.processes.dtos.CreateProcessRequest
import com.example.afaloan.controllers.processes.dtos.ProcessDto
import com.example.afaloan.controllers.profiles.dtos.CreateProfileRequest
import com.example.afaloan.controllers.profiles.dtos.UpdateProfileRequest
import com.example.afaloan.models.Profile
import com.example.afaloan.models.Bid
import com.example.afaloan.models.BoilingPoint
import com.example.afaloan.models.Microloan
import com.example.afaloan.models.Process
import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.models.enumerations.ProcessStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

val USER_ID: UUID = UUID.randomUUID()

const val USERNAME = "username"

const val ROLE_SUPERVISOR = "SUPERVISOR"

const val ROLE_WORKER = "WORKER"

const val ROLE_CUSTOMER = "CUSTOMER"

/**
 * Profile
 */

fun createProfile() = Profile(
    id = UUID.randomUUID(),
    name = "name",
    surname = "surname",
    patronymic = "patronymic",
    phoneNumber = "+79832422045",
    passportSeries = "1234",
    passportNumber = "123456",
    monthlyIncome = BigDecimal.TEN,
    userId = USER_ID
)

fun createCreateProfileRequest() = CreateProfileRequest(
    name = "name",
    surname = "surname",
    patronymic = "patronymic",
    phoneNumber = "+79832422045",
    passportSeries = "1234",
    passportNumber = "123456",
    monthlyIncome = BigDecimal.TEN
)

fun createUpdateProfileRequest() = UpdateProfileRequest(
    name = "name",
    surname = "surname",
    patronymic = "patronymic",
    phoneNumber = "+79832422045",
    passportSeries = "1234",
    passportNumber = "123456",
    monthlyIncome = BigDecimal.TEN
)

/**
 * Microloan
 */

fun createMicroloan() = Microloan(
    id = UUID.randomUUID(),
    name = "name",
    sum = BigDecimal.valueOf(100),
    monthlyInterest = BigDecimal.ZERO,
    conditions = "conditions",
    monthlyIncomeRequirement = BigDecimal.TWO,
    otherRequirements = "other requirements"
)

fun createMicroloanDto() = MicroloanDto(
    name = "name",
    sum = BigDecimal.TWO,
    monthlyInterest = BigDecimal.ZERO,
    conditions = "conditions",
    monthlyIncomeRequirement = BigDecimal.ONE,
    otherRequirements = "other requirements"
)

/**
 * BoilingPoint
 */

fun createBoilingPoint() = BoilingPoint(
    id = UUID.randomUUID(),
    city = "Taishet",
    address = "Cherniy vanya 3a",
    openingHours = "Пн-Пт: 09:00-18:00, Сб: 10:00-14:00, Вс: выходной",
    info = "info"
)

fun createCreateBoilingPointRequest() = CreateBoilingPointRequest(
    city = "Taishet",
    address = "Cherniy vanya 3a",
    openingHours = "Пн-Пт: 09:00-18:00, Сб: 10:00-14:00, Вс: выходной",
    info = "info"
)

fun createUpdateBoilingPointRequest() = UpdateBoilingPointRequest(
    city = "Taishet",
    address = "Cherniy vanya 3a",
    openingHours = "Пн-Пт: 09:00-18:00, Сб: 10:00-14:00, Вс: выходной",
    info = "info"
)

/**
 * Bid
 */

fun createBid(
    profile: Profile = createProfile(),
    microloan: Microloan = createMicroloan(),
    boilingPoint: BoilingPoint = createBoilingPoint()
) = Bid(
    id = UUID.randomUUID(),
    target = "target",
    coverLetter = "cover letter",
    date = LocalDateTime.now(),
    priority = BidPriority.MEDIUM,
    status = BidStatus.UNDER_CONSIDERATION,
    employeeMessage = "employee message",
    profile = profile,
    microloan = microloan,
    boilingPoint = boilingPoint
)

fun createCreateBidRequest(bid: Bid = createBid()) = CreateBidRequest(
    target = bid.target,
    coverLetter = bid.coverLetter,
    priority = bid.priority,
    employeeMessage = bid.employeeMessage,
    profileId = bid.profile!!.id!!,
    microloanId = bid.microloan!!.id!!,
    boilingPointId = bid.boilingPoint!!.id!!
)

/**
 * Process
 */

fun createProcess(bid: Bid = createBid()) = Process(
    id = UUID.randomUUID(),
    debt = bid.microloan!!.sum,
    status = ProcessStatus.IN_PROCESSING,
    comment = "comment",
    bid = bid
)

fun createCreateProcessRequest(process: Process = createProcess()) = CreateProcessRequest(
    debt = process.debt,
    comment = process.comment,
    bidId = process.bid!!.id!!
)

fun createProcessDto(process: Process = createProcess()) = ProcessDto(
    debt = process.debt,
    comment = process.comment,
    status = ProcessStatus.IN_PROCESSING,
    bidId = process.bid!!.id!!
)
