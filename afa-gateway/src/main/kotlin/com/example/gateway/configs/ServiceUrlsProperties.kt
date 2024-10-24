package com.example.gateway.configs

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.services.urls")
data class ServiceUrlsProperties@ConstructorBinding constructor(
    val afaUser: String,
    val afaloan: String
)
