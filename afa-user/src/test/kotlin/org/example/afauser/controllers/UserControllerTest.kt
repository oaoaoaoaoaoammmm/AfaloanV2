package org.example.afauser.controllers

import org.assertj.core.api.Assertions.assertThat
import org.example.afauser.BaseIntegrationTest
import org.example.afauser.controllers.users.dtos.UserDto
import org.example.afauser.utils.USER
import org.example.afauser.utils.createTestAuthentication
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockAuthentication

class UserControllerTest : BaseIntegrationTest() {

    @Test
    fun `find should return OK`() {
        client.get()
            .uri("/users/${USER.id}")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectAll(
                { it.expectStatus().isOk() },
                {
                    it.expectBody(UserDto::class.java)
                        .value { user ->
                            assertThat(user.id).isEqualTo(USER.id)
                            assertThat(user.username).isEqualTo(USER.username)
                        }
                },
            )
    }

    /*
    @Test
    fun `delete should return NO_CONTENT`() {
        mockMvc.perform(
            delete("$/users/${USER.id}")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `updateRoles should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$/users/${USER.id}/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    UpdateRoleRequest(Role.WORKER).toJson()
                )
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `block should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$/users/${USER.id}/block")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `unblock should return NO_CONTENT`() {
        mockMvc.perform(
            delete("$/users/${USER.id}/block")
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `confirm should return NO_CONTENT`() {
        mockMvc.perform(
            patch("$/users/${USER.id}/confirm")
        ).andExpect(status().isNoContent)
    }

     */
}