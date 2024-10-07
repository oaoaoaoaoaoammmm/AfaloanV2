package com.example.afaadmin

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
class AfaAdminApplication

fun main(args: Array<String>) {
	runApplication<AfaAdminApplication>(*args)
}
