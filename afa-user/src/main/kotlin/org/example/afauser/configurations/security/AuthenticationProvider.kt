package org.example.afauser.configurations.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.example.afauser.models.enumerations.Role
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import javax.crypto.SecretKey

@Component
class AuthenticationProvider(
    private val props: AuthenticationProperties,
) {

    private lateinit var key: SecretKey

    @PostConstruct
    fun init() {
        this.key = Keys.hmacShaKeyFor(props.secret.toByteArray())
    }

    fun isValid(token: String): Boolean {
        val jwtParser = Jwts.parser().verifyWith(key).build()
        val claims = jwtParser.parseSignedClaims(token)
        return claims.payload.expiration.after(Date())
    }

    fun createAccessToken(userId: UUID, username: String, roles: Set<Role>): String {
        val claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .add("roles", roles)
            .build()
        val validity = Instant.now().plus(props.access, ChronoUnit.HOURS)
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact()
    }

    fun createRefreshToken(userId: UUID, username: String): String {
        val claims = Jwts.claims()
            .subject(username)
            .add("id", userId)
            .build()
        val validity = Instant.now().plus(props.refresh, ChronoUnit.HOURS)
        return Jwts.builder()
            .claims(claims)
            .expiration(Date.from(validity))
            .signWith(key)
            .compact()
    }

    fun getAuthentication(token: String) = UsernamePasswordAuthenticationToken(token.id, token.username, token.roles)

    fun getIdFromToken(token: String) = token.id

    private val String.id: String
        get() {
            val jwtParser = Jwts.parser().verifyWith(key).build()
            return jwtParser.parseSignedClaims(this).payload.get("id", String::class.java)
        }

    private val String.username: String
        get() {
            val jwtParser = Jwts.parser().verifyWith(key).build()
            return jwtParser.parseSignedClaims(this).payload.subject
        }

    private val String.roles: List<GrantedAuthority>
        get() {
            val jwtParser = Jwts.parser().verifyWith(key).build()
            val roles = jwtParser.parseSignedClaims(this).payload["roles"] as List<*>
            return roles.map { it as String }
                .map { SimpleGrantedAuthority(it) }
        }
}
