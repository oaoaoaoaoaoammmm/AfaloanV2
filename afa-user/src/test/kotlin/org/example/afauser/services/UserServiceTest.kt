package org.example.afauser.services

import org.example.afauser.exceptions.InternalException
import org.example.afauser.models.User
import org.example.afauser.models.enumerations.Role
import org.example.afauser.repositories.UserRepository
import org.example.afauser.utils.UNAUTHORIZED_USER
import org.example.afauser.utils.USER
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import reactor.core.publisher.Mono
import reactor.kotlin.test.expectError
import reactor.test.StepVerifier
import java.util.UUID

class UserServiceTest {

    private val userRepository = mock<UserRepository>()

    private val userService = UserService(userRepository)

    @Test
    fun `isExists should return true`() {
        whenever(userRepository.existsByUsername(any())).thenReturn(Mono.just(true))

        StepVerifier.create(userService.isExists(USER.username))
            .expectNext(true)
            .expectComplete()
            .verify()
    }

    @Test
    fun `find(username String) should execute successfully`() {
        whenever(userRepository.findByUsername(any())).thenReturn(Mono.just(USER))

        StepVerifier.create(userService.find(USER.username))
            .expectNext(USER)
            .expectComplete()
            .verify()
    }

    @Test
    fun `find(username String) should throw USER_NOT_FOUND`() {
        whenever(userRepository.findByUsername(any())).thenReturn(Mono.empty())

        StepVerifier.create(userService.find(UNAUTHORIZED_USER.username))
            .expectError(InternalException::class)
            .verify()
    }


    @Test
    fun `updateRoles should execute successfully`() {
        whenever(userRepository.findById(USER.id!!)).thenReturn(Mono.just(USER))
        whenever(userRepository.save(any<User>())).thenReturn(Mono.just(USER.copy(role = Role.WORKER)))

        StepVerifier.create(userService.updateRoles(USER.id!!, Role.WORKER))
            .expectNext(USER.copy(role = Role.WORKER))
            .expectComplete()
            .verify()
    }

    @Test
    fun `block should execute successfully`() {
        whenever(userRepository.findById(USER.id!!)).thenReturn(Mono.just(USER))
        whenever(userRepository.save(any<User>())).thenReturn(Mono.just(USER.copy(blocked = true)))

        StepVerifier.create(userService.block(USER.id!!))
            .expectNext(USER.copy(blocked = true))
            .expectComplete()
            .verify()
    }


    @Test
    fun `unblock should execute successfully`() {
        whenever(userRepository.findById(USER.id!!)).thenReturn(Mono.just(USER))
        whenever(userRepository.save(any<User>())).thenReturn(Mono.just(USER.copy(blocked = true)))

        StepVerifier.create(userService.unblock(USER.id!!))
            .expectNext(USER.copy(blocked = true))
            .expectComplete()
            .verify()
    }

    @Test
    fun `confirm should execute successfully`() {
        whenever(userRepository.findById(USER.id!!)).thenReturn(Mono.just(USER))
        whenever(userRepository.save(any<User>())).thenReturn(Mono.just(USER.copy(confirmed = true)))

        StepVerifier.create(userService.unblock(USER.id!!))
            .expectNext(USER.copy(confirmed = true))
            .expectComplete()
            .verify()
    }


    @Test
    fun `delete(id UUID) should execute successfully`() {
        whenever(userRepository.findById(any<UUID>())).thenReturn(Mono.just(USER))
        whenever(userRepository.delete(any<User>())).thenReturn(Mono.empty())

        StepVerifier.create(userService.delete(USER.id!!))
            .expectNext()
            .expectComplete()
            .verify()
    }

    @Test
    fun `delete(username String) should execute successfully`() {
        whenever(userRepository.findByUsername(any<String>())).thenReturn(Mono.just(USER))
        whenever(userRepository.delete(any<User>())).thenReturn(Mono.empty())

        StepVerifier.create(userService.delete(USER.username))
            .expectNext()
            .expectComplete()
            .verify()
    }
}