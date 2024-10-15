package com.example.afaloan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
class AfaloanApplication

fun main(args: Array<String>) {
    runApplication<AfaloanApplication>(*args)
}
