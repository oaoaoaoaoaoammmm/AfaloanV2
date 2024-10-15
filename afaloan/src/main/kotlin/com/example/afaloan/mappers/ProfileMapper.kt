package com.example.afaloan.mappers

import com.example.afaloan.controllers.profiles.dtos.CreateProfileRequest
import com.example.afaloan.controllers.profiles.dtos.ProfileDto
import com.example.afaloan.controllers.profiles.dtos.UpdateProfileRequest
import com.example.afaloan.models.Profile
import com.example.afaloan.utils.SecurityContext
import org.springframework.stereotype.Component

@Component
class ProfileMapper {

    fun convert(request: CreateProfileRequest): Profile {
        return Profile(
            name = request.name,
            surname = request.surname,
            patronymic = request.patronymic,
            phoneNumber = request.phoneNumber,
            passportSeries = request.passportSeries,
            passportNumber = request.passportNumber,
            snils = request.snils,
            inn = request.inn,
            monthlyIncome = request.monthlyIncome,
            userId = SecurityContext.getAuthorizedUserId()
        )
    }

    fun convert(request: UpdateProfileRequest): Profile {
        return Profile(
            name = request.name,
            surname = request.surname,
            patronymic = request.patronymic,
            phoneNumber = request.phoneNumber,
            passportSeries = request.passportSeries,
            passportNumber = request.passportNumber,
            monthlyIncome = request.monthlyIncome,
            userId = SecurityContext.getAuthorizedUserId()
        )
    }

    fun convert(profile: Profile): ProfileDto {
        return ProfileDto(
            id = profile.id,
            name = profile.name,
            surname = profile.surname,
            patronymic = profile.patronymic,
            phoneNumber = profile.phoneNumber,
            passportSeries = profile.passportSeries,
            passportNumber = profile.passportNumber,
            snils = profile.snils,
            inn = profile.inn,
            monthlyIncome = profile.monthlyIncome,
        )
    }
}