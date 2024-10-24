package com.example.afaadmin

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AfaAdminApplicationTests {

	@Test
	fun contextLoads() {
		assert(true)
	}
}
