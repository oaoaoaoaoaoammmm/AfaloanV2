package com.example.afaloan.repositories

import com.example.afaloan.models.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface ProfileRepository: JpaRepository<Profile, UUID> {

    fun findByUserId(userId: UUID): Profile?

    fun findByIdAndUserId(id: UUID, userId: UUID): Profile?
}