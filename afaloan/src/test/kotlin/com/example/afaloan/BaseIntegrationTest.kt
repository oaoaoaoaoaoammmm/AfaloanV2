package com.example.afaloan

import com.example.afaloan.configurations.PostgresAutoConfiguration
import com.example.afaloan.utils.mockSecurityContext
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(
    classes = [PostgresAutoConfiguration::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
abstract class BaseIntegrationTest {

    protected lateinit var mockMvc: MockMvc

    @BeforeEach
    fun setUp(context: WebApplicationContext) {
        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        mockSecurityContext()
    }

    @AfterEach
    fun tearDown() {
        SecurityContextHolder.clearContext()
    }
}
