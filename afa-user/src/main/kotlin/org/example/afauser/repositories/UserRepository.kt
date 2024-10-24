package org.example.afauser.repositories

import org.example.afauser.models.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Mono
import java.util.UUID

interface UserRepository : ReactiveCrudRepository<User, UUID> {

    fun existsByUsername(username: String): Mono<Boolean>

    fun findByUsername(username: String): Mono<User>
}
