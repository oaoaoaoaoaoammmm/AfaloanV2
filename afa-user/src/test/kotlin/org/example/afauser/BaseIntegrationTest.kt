package org.example.afauser

import org.example.afauser.configurations.PostgresAutoConfiguration
import org.example.afauser.repositories.UserRepository
import org.example.afauser.utils.USER
import org.example.afauser.utils.createTestAuthentication
import org.example.afauser.utils.mockSecurityContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.*
import org.springframework.test.web.servlet.setup.MockMvcConfigurer
import reactor.kotlin.core.publisher.switchIfEmpty

@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
abstract class BaseIntegrationTest : PostgresAutoConfiguration() {

    @Autowired
    protected lateinit var client: WebTestClient

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp(context: ApplicationContext) {
        createUserIfNotExist()
        mockSecurityContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    private fun createUserIfNotExist() {
        USER = userRepository.findById(USER.id!!)
            .switchIfEmpty { userRepository.save(USER.copy(id = null)) }
            .block()!!
    }
}
