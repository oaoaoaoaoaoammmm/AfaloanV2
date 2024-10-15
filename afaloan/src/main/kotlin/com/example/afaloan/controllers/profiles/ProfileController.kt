package com.example.afaloan.controllers.profiles

import com.example.afaloan.controllers.profiles.ProfileController.Companion.ROOT_URI
import com.example.afaloan.controllers.profiles.dtos.CreateProfileRequest
import com.example.afaloan.controllers.profiles.dtos.CreateProfileResponse
import com.example.afaloan.controllers.profiles.dtos.ProfileDto
import com.example.afaloan.controllers.profiles.dtos.UpdateProfileRequest
import com.example.afaloan.mappers.ProfileMapper
import com.example.afaloan.services.ProfileService
import com.example.afaloan.utils.logger
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(ROOT_URI)
class ProfileController(
    private val profileMapper: ProfileMapper,
    private val profileService: ProfileService
) {

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun find(@PathVariable id: UUID): ProfileDto {
        logger.trace { "Find profile by id - $id" }
        val profile = profileService.find(id)
        return profileMapper.convert(profile)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateProfileRequest): CreateProfileResponse {
        logger.trace { "Create profile for ${request.name} ${request.surname}" }
        val profile = profileMapper.convert(request)
        val profileId = profileService.create(profile)
        return CreateProfileResponse(profileId)
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateProfileRequest
    ): ProfileDto {
        logger.trace { "Update profile by id - $id" }
        val newProfile = profileMapper.convert(request)
        val updatedProfile = profileService.update(id, newProfile)
        return profileMapper.convert(updatedProfile)
    }

    companion object {
        const val ROOT_URI = "/profiles"
    }
}