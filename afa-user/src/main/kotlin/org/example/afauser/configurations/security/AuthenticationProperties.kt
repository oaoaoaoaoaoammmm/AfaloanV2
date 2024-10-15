package org.example.afauser.configurations.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.security.jwt")
data class AuthenticationProperties@ConstructorBinding constructor(
    val secret: String,
    val access: Long,
    val refresh: Long
)
