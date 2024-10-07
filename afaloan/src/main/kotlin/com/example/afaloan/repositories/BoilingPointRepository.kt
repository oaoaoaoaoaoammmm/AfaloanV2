package com.example.afaloan.repositories

import com.example.afaloan.models.BoilingPoint
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface BoilingPointRepository: JpaRepository<BoilingPoint, UUID>