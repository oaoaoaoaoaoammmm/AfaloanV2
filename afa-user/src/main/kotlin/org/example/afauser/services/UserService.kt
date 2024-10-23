package org.example.afauser.services

import org.example.afauser.exceptions.ErrorCode
import org.example.afauser.exceptions.InternalException
import org.example.afauser.models.User
import org.example.afauser.models.enumerations.Role
import org.example.afauser.repositories.UserRepository
import org.example.afauser.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun isExists(username: String): Mono<Boolean> {
        logger.info { "Check on exists for user with username - $username" }
        return userRepository.existsByUsername(username)
    }

    fun find(id: UUID): Mono<User> {
        logger.info { "Finding user by id - $id" }
        return userRepository.findById(id).switchIfEmpty(
            Mono.error(InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.USER_NOT_FOUND))
        )
    }

    fun find(username: String): Mono<User> {
        logger.info { "Finding user by username - $username" }
        return userRepository.findByUsername(username).switchIfEmpty(
            Mono.error(InternalException(httpStatus = HttpStatus.NOT_FOUND, errorCode = ErrorCode.USER_NOT_FOUND))
        )
    }

    fun create(user: User): Mono<User> {
        logger.info { "Creating user with username - ${user.username}" }
        return userRepository.save(user)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateRoles(id: UUID, role: Role): Mono<User> {
        return find(id).flatMap { user ->
            val updatedUser = user.copy(role = role)
            logger.info { "User updating role - $role with id - $id" }
            return@flatMap userRepository.save(updatedUser)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun block(id: UUID): Mono<User> {
        return find(id).flatMap { user ->
            val updatedUser = user.copy(blocked = true)
            logger.info { "User blocking with id - $id" }
            return@flatMap userRepository.save(updatedUser)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun unblock(id: UUID): Mono<User> {
        return find(id).flatMap { user ->
            val updatedUser = user.copy(blocked = false)
            logger.info { "User unblocking with id - $id" }
            return@flatMap userRepository.save(updatedUser)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun confirm(id: UUID): Mono<User> {
        return find(id).flatMap { user ->
            val updatedUser = user.copy(confirmed = true)
            logger.info { "User confirmed with id - $id" }
            return@flatMap userRepository.save(updatedUser)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun delete(id: UUID): Mono<Void> {
        return find(id).flatMap { user ->
            logger.info { "Deleting user by id - $id" }
            return@flatMap userRepository.delete(user)
        }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun delete(username: String): Mono<Void> {
        return find(username).flatMap { user ->
            logger.info { "Deleting user by username - $username" }
            return@flatMap userRepository.delete(user)
        }
    }
}