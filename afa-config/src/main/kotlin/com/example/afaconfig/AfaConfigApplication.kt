package com.example.afaconfig

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.config.server.EnableConfigServer

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
class AfaConfigApplication

fun main(args: Array<String>) {
	runApplication<AfaConfigApplication>(*args)
}
