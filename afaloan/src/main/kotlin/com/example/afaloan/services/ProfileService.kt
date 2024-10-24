package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.Profile
import com.example.afaloan.repositories.ProfileRepository
import com.example.afaloan.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ProfileService(
    private val profileRepository: ProfileRepository
) {

    fun find(id: UUID): Profile {
        logger.info { "Finding profile by id - $id" }
        return profileRepository.findById(id).orElseThrow {
            InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.PROFILE_NOT_FOUND)
        }
    }

    fun find(id: UUID, userId: UUID): Profile {
        logger.info { "Finding profile by id - $id" }
        return profileRepository.findByIdAndUserId(id, userId)
            ?: throw InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.PROFILE_NOT_FOUND)
    }

    fun findByUserId(userId: UUID): Profile {
        logger.info { "Find profile by user id - $userId" }
        return profileRepository.findByUserId(userId)
            ?: throw InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.PROFILE_NOT_FOUND)
    }

    fun create(profile: Profile): UUID {
        logger.info { "Creating profile for user with id - ${profile.userId}" }
        return profileRepository.save(profile).id!!
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun update(id: UUID, profile: Profile): Profile {
        logger.info { "Updating profile for user with id - ${profile.userId}" }
        val oldProfile = find(id, profile.userId)
        val newProfile = profile.copy(
            id = oldProfile.id,
            snils = oldProfile.snils,
            inn = oldProfile.inn,
            userId = oldProfile.userId
        )
        return profileRepository.save(newProfile)
    }
}