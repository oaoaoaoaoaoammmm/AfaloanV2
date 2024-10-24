package org.example.afauser

import org.example.afauser.configurations.security.AuthenticationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties(AuthenticationProperties::class)
class AfaUserApplication

fun main(args: Array<String>) {
	runApplication<AfaUserApplication>(*args)
}
