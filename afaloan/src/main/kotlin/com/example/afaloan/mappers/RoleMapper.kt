package com.example.afaloan.mappers

import com.example.afaloan.models.UserRole
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.services.RoleService
import org.springframework.stereotype.Component

@Component
class RoleMapper(
    private val roleService: RoleService
) {

    fun convert(roles: List<Role>): Set<UserRole> {
        val userRoles = roleService.findAll()
        return userRoles.filter { roles.contains(it.role) }.toSet()
    }
}