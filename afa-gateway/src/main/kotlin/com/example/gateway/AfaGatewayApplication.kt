package com.example.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class AfaGatewayApplication

fun main(args: Array<String>) {
	runApplication<AfaGatewayApplication>(*args)
}
