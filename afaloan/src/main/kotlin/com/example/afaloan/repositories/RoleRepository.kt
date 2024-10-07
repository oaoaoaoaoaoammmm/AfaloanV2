package com.example.afaloan.repositories

import com.example.afaloan.models.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface RoleRepository: JpaRepository<UserRole, UUID>