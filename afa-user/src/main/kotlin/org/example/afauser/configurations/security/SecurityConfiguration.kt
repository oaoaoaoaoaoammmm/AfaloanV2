package org.example.afauser.configurations.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfiguration(
    private val authenticationFilter: AuthenticationFilter
) {

    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .cors { cors -> cors.disable() }
            .csrf { csrf -> csrf.disable() }
            .formLogin { formLogin -> formLogin.disable() }
            .httpBasic { httpBasic -> httpBasic.disable() }
            .anonymous { anonymous -> anonymous.disable() }
            .sessionManagement { SessionCreationPolicy.STATELESS }
            .authorizeExchange { exchanges ->
                exchanges.pathMatchers(
                    "/actuator/**",
                    "/webjars/**",
                    "/v3/api-docs/**",
                    "/auth/**"
                ).permitAll()
                exchanges.pathMatchers("/**").authenticated()
            }
            .addFilterBefore(authenticationFilter, SecurityWebFiltersOrder.AUTHORIZATION)
            .exceptionHandling {
                it.authenticationEntryPoint { exchange, _ ->
                    Mono.fromRunnable { exchange.response.statusCode = HttpStatus.UNAUTHORIZED }
                }
                it.accessDeniedHandler { exchange, _ ->
                    Mono.fromRunnable { exchange.response.statusCode = HttpStatus.FORBIDDEN }
                }
            }
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
