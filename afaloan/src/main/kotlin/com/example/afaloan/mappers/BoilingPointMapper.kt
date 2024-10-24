package com.example.afaloan.mappers

import com.example.afaloan.controllers.boilingpoints.dtos.CreateBoilingPointRequest
import com.example.afaloan.controllers.boilingpoints.dtos.UpdateBoilingPointRequest
import com.example.afaloan.models.BoilingPoint
import org.springframework.stereotype.Component

@Component
class BoilingPointMapper {

    fun convert(request: CreateBoilingPointRequest): BoilingPoint {
        return BoilingPoint(
            id = null,
            city = request.city,
            address = request.address,
            openingHours = request.openingHours,
            info = request.info
        )
    }

    fun convert(request: UpdateBoilingPointRequest): BoilingPoint {
        return BoilingPoint(
            id = null,
            city = request.city,
            address = request.address,
            openingHours = request.openingHours,
            info = request.info
        )
    }
}