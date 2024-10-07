package com.example.afaloan

import com.example.afaloan.configurations.PostgresAutoConfiguration
import com.example.afaloan.repositories.RoleRepository
import com.example.afaloan.repositories.UserRepository
import com.example.afaloan.utils.USER
import com.example.afaloan.utils.mockSecurityContext
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.testcontainers.containers.MinIOContainer
import org.testcontainers.utility.DockerImageName

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [PostgresAutoConfiguration::class]
)
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

    protected lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userRoleRepository: RoleRepository

    @BeforeEach
    fun setUp(context: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        createUserIfNotExist()
        mockSecurityContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }

    private fun createUserIfNotExist() {
        val user = userRepository.findById(USER.id!!)
        if (user.isEmpty) {
            val roles = userRoleRepository.findAll().toSet()
            USER = userRepository.save(USER.copy(roles = roles))
        }
    }

    companion object {
        const val API_PREFIX = "/api/v1"

        @JvmStatic
        val minioContainer = MinIOContainer(
            DockerImageName.parse("minio/minio:RELEASE.2024-08-03T04-33-23Z")
        )

        @BeforeAll
        @JvmStatic
        fun startContainers() {
            minioContainer.start()
        }

        @AfterAll
        @JvmStatic
        fun stopContainers() {
            minioContainer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.s3.endpoint") { minioContainer.s3URL }
            registry.add("spring.s3.access-key") { minioContainer.userName }
            registry.add("spring.s3.secret-key") { minioContainer.password }
            registry.add("spring.s3.bucket") { "afaloan" }
            registry.add("spring.s3.region") { "us-east-1" }
        }
    }
}
