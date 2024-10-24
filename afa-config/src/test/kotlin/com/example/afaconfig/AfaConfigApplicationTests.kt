package com.example.afaconfig

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class AfaConfigApplicationTests {

	@Test
	fun contextLoads() {
		assert(true)
	}
}
