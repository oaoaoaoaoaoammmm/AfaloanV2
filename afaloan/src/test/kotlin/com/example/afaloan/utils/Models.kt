package com.example.afaloan.utils

import com.example.afaloan.controller.bids.dtos.CreateBidRequest
import com.example.afaloan.controller.boilingpoints.dtos.CreateBoilingPointRequest
import com.example.afaloan.controller.boilingpoints.dtos.UpdateBoilingPointRequest
import com.example.afaloan.controller.microloans.dtos.MicroloanDto
import com.example.afaloan.controller.processes.dtos.CreateProcessRequest
import com.example.afaloan.controller.processes.dtos.ProcessDto
import com.example.afaloan.controller.profiles.dtos.CreateProfileRequest
import com.example.afaloan.controller.profiles.dtos.UpdateProfileRequest
import com.example.afaloan.models.Bid
import com.example.afaloan.models.User
import com.example.afaloan.models.BoilingPoint
import com.example.afaloan.models.Microloan
import com.example.afaloan.models.Process
import com.example.afaloan.models.Profile
import com.example.afaloan.models.UserRole
import com.example.afaloan.models.enumerations.BidPriority
import com.example.afaloan.models.enumerations.BidStatus
import com.example.afaloan.models.enumerations.ProcessStatus
import com.example.afaloan.models.enumerations.Role
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

const val USER_PASSWORD = "12345"

const val ENCODED_USER_PASSWORD = "\$2a\$10\$.IEUyyxTZjIGYnDHOcFW3e8AD5QFAKWj7nu7NM1NfBs.wE6AtC83a"

var USER = User(
    id = UUID.randomUUID(),
    username = "john.doe@mail.ru",
    password = ENCODED_USER_PASSWORD,
    confirmed = true,
    confirmedUsername = true,
    blocked = false,
    roles = setOf()
)

val UNAUTHORIZED_USER = User(
    id = UUID.randomUUID(),
    username = "johan.do@mail.ru",
    password = "12345678",
    confirmed = false,
    confirmedUsername = false,
    blocked = false,
    roles = setOf(UserRole(id = UUID.randomUUID(), Role.CUSTOMER))
)

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
    user = USER
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
