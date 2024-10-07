package com.example.afaloan

import com.example.afaloan.configurations.s3.S3Properties
import com.example.afaloan.configurations.security.AuthenticationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@EnableDiscoveryClient
@SpringBootApplication
@EnableConfigurationProperties(S3Properties::class, AuthenticationProperties::class)
class AfaloanApplication

fun main(args: Array<String>) {
    runApplication<AfaloanApplication>(*args)
}
