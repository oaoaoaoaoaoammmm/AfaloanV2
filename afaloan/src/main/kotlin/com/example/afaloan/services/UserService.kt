package com.example.afaloan.services

import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.models.User
import com.example.afaloan.models.UserRole
import com.example.afaloan.repositories.UserRepository
import com.example.afaloan.utils.SecurityContext
import com.example.afaloan.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val roleService: RoleService,
    private val userRepository: UserRepository
) {

    fun isExists(username: String): Boolean {
        logger.info { "Check on exists for user with username - $username" }
        return userRepository.existsByUsername(username)
    }

    fun find(id: UUID): User {
        logger.info { "Finding user by id - $id" }
        return userRepository.findById(id).orElseThrow {
            throw InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.USER_NOT_FOUND)
        }
    }

    fun find(username: String): User {
        logger.info { "Finding user by username - $username" }
        return userRepository.findByUsername(username) ?:
            throw InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.USER_NOT_FOUND)
    }

    fun create(user: User): User {
        logger.info { "Creating user with username - ${user.username}" }
        return userRepository.save(user)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateRoles(id: UUID, roles: Set<UserRole>): User {
        val user = find(id)
        if (!roleService.isExists(roles.map(UserRole::role).toSet())) {
            throw InternalException(httpStatus = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.ROLE_NOT_FOUND)
        }
        val updatedUser = user.copy(roles = roles)
        logger.info { "Updating roles for user with id - $id" }
        return userRepository.save(updatedUser)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun block(id: UUID) {
        val user = find(id)
        val updatedUser = user.copy(blocked = true)
        logger.info { "User blocking with id - $id" }
        userRepository.save(updatedUser)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun unblock(id: UUID) {
        val user = find(id)
        val updatedUser = user.copy(blocked = false)
        logger.info { "User unblocking with id - $id" }
        userRepository.save(updatedUser)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun confirm(id: UUID) {
        val user = find(id)
        val updatedUser = user.copy(confirmed = true)
        logger.info { "User confirmed with id - $id" }
        userRepository.save(updatedUser)
    }

    @Transactional
    fun confirmUsername(id: UUID) {
        val user = find(id)
        val userId = SecurityContext.getAuthorizedUserId()
        if (user.id != userId) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.FORBIDDEN)
        }
        TODO("Not implemented yet, mb send email?")
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun delete(id: UUID) {
        val user = find(id)
        val userId = SecurityContext.getAuthorizedUserId()
        if (user.id != userId) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.FORBIDDEN)
        }
        logger.info { "Deleting user by id - $id" }
        userRepository.deleteById(id)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun delete(username: String) {
        val user = find(username)
        val userId = SecurityContext.getAuthorizedUserId()
        if (user.id != userId) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.FORBIDDEN)
        }
        logger.info { "Deleting user by username - $username" }
        userRepository.deleteByUsername(username)
    }
}