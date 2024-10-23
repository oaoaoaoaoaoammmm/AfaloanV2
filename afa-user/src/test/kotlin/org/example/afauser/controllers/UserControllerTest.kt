package org.example.afauser.controllers

import org.assertj.core.api.Assertions.assertThat
import org.example.afauser.BaseIntegrationTest
import org.example.afauser.controllers.users.dtos.UpdateRoleRequest
import org.example.afauser.controllers.users.dtos.UserDto
import org.example.afauser.models.enumerations.Role
import org.example.afauser.utils.USER
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType

class UserControllerTest : BaseIntegrationTest() {

    @Test
    fun `find should return OK`() {
        client.get()
            .uri("/users/${USER.id}")
            .exchange()
            .expectAll(
                { it.expectStatus().isOk },
                { it.expectBody(UserDto::class.java).value { user ->
                        assertThat(user.id).isEqualTo(USER.id)
                        assertThat(user.username).isEqualTo(USER.username)
                    }
                },
            )
    }

    @Test
    fun `delete should return NO_CONTENT`() {
        client.delete()
            .uri("/users/${USER.id}")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `updateRoles should return NO_CONTENT`() {
        client.patch()
            .uri("/users/${USER.id}/roles")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(UpdateRoleRequest(Role.WORKER))
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `block should return NO_CONTENT`() {
        client.patch()
            .uri("/users/${USER.id}/block")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `unblock should return NO_CONTENT`() {
        client.delete()
            .uri("/users/${USER.id}/block")
            .exchange()
            .expectStatus().isNoContent
    }

    @Test
    fun `confirm should return NO_CONTENT`() {
        client.patch()
            .uri("/users/${USER.id}/confirm")
            .exchange()
            .expectStatus().isNoContent
    }
}