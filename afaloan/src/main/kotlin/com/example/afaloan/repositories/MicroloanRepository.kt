package com.example.afaloan.repositories

import com.example.afaloan.models.Microloan
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface MicroloanRepository: JpaRepository<Microloan, UUID>