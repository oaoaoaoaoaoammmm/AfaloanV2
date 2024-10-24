package org.example.afauser

import org.example.afauser.configurations.PostgresAutoConfiguration
import org.example.afauser.repositories.UserRepository
import org.example.afauser.utils.USER
import org.example.afauser.utils.createAuthentication
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.kotlin.core.publisher.switchIfEmpty

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class BaseIntegrationTest : PostgresAutoConfiguration() {

    protected lateinit var client: WebTestClient

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp(context: ApplicationContext) {
        createUserIfNotExist()
        client = WebTestClient.bindToApplicationContext(context)
            .apply(springSecurity())
            .configureClient()
            .build()
            .mutateWith(mockAuthentication(createAuthentication()))
    }

    private fun createUserIfNotExist() {
        USER = userRepository.findById(USER.id!!)
            .switchIfEmpty { userRepository.save(USER.copy(id = null)) }
            .block()!!
    }
}
