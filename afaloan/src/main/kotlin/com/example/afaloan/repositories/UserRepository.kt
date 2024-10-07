package com.example.afaloan.repositories

import com.example.afaloan.models.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository: JpaRepository<User, UUID> {

    fun findByUsername(username: String): User?

    fun deleteByUsername(username: String)

    fun existsByUsername(username: String): Boolean
}