package com.example.afaloan.services

import com.example.afaloan.models.UserRole
import com.example.afaloan.models.enumerations.Role
import com.example.afaloan.repositories.RoleRepository
import com.example.afaloan.utils.logger
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleRepository: RoleRepository
) {

    fun findAll(): List<UserRole> {
        logger.info { "Finding all roles" }
        return roleRepository.findAll()
    }

    fun isExists(role: Role): Boolean {
        logger.info { "Checking on exist role - $role" }
        val existingRoles = findAll().map(UserRole::role)
        return existingRoles.contains(role)
    }

    fun isExists(roles: Set<Role>): Boolean {
        logger.info { "Checking on exist roles - $roles" }
        val existingRoles = findAll().map(UserRole::role)
        return existingRoles.containsAll(roles)
    }
}