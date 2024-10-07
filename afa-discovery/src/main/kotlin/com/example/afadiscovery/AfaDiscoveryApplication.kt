package com.example.afadiscovery

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer

@EnableEurekaServer
@SpringBootApplication
class AfaDiscoveryApplication

fun main(args: Array<String>) {
	runApplication<AfaDiscoveryApplication>(*args)
}
