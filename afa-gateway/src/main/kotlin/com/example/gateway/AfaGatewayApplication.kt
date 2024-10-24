package com.example.gateway

import com.example.gateway.configs.ServiceUrlsProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(ServiceUrlsProperties::class)
class AfaGatewayApplication

fun main(args: Array<String>) {
	runApplication<AfaGatewayApplication>(*args)
}
