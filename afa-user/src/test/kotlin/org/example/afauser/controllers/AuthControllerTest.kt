package org.example.afauser.controllers

import org.example.afauser.BaseIntegrationTest
import org.example.afauser.controllers.auth.dtos.AuthorizeUserRequest
import org.example.afauser.controllers.auth.dtos.AuthorizeUserResponse
import org.example.afauser.controllers.auth.dtos.RegisterUserRequest
import org.example.afauser.controllers.auth.dtos.RegisterUserResponse
import org.example.afauser.models.enumerations.Role
import org.example.afauser.utils.UNAUTHORIZED_USER
import org.example.afauser.utils.USER
import org.example.afauser.utils.USER_PASSWORD
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

@Disabled
class AuthControllerTest : BaseIntegrationTest() {

    @Test
    fun `register should return CREATED`() {
        client.post()
            .uri("/auth")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                RegisterUserRequest(
                    username = UNAUTHORIZED_USER.username,
                    password = UNAUTHORIZED_USER.password,
                    role = Role.WORKER
                )
            )
            .exchange()
            .expectAll(
                { it.expectStatus().isCreated },
                { it.expectBody(RegisterUserResponse::class.java) },
            )
    }


    @Test
    fun `authorize should return CREATED`() {
        client.post()
            .uri("/auth/tokens")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                AuthorizeUserRequest(
                    username = USER.username,
                    password = USER_PASSWORD
                )
            )
            .exchange()
            .expectAll(
                { it.expectStatus().isCreated },
                { it.expectBody(AuthorizeUserResponse::class.java) },
            )
    }

    @Test
    fun `reAuthorize should return BAD_REQUEST`() {
        client.post()
            .uri("/auth/tokens/refresh?refresh=refresh")
            .contentType(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isBadRequest
    }
}