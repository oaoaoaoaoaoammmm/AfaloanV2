package com.example.afaloan.controllers

import com.example.afaloan.BaseIntegrationTest
import com.example.afaloan.controller.files.dtos.UploadFileResponse
import com.example.afaloan.utils.toObject
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Disabled
class FileControllerTest : BaseIntegrationTest() {

    @Test
    fun `upload should return CREATED`() {
        uploadDocument()
    }

    @Test
    fun `download should execute OK`() {
        val uploadFileResponse = uploadDocument()
        val mvcResult = mockMvc.perform(
            get("$API_PREFIX/files")
                .param("path", uploadFileResponse.path)
        ).andExpect(status().isOk).andReturn()

        assertThat(mvcResult.response.contentType).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE)
    }

    @Test
    fun `delete should return NO_CONTENT`() {
        val uploadFileResponse = uploadDocument()
        mockMvc.perform(
            delete("$API_PREFIX/files")
                .param("path", uploadFileResponse.path)
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `findPreviews should return OK`() {
        val uploadFileResponse = uploadDocument()
        mockMvc.perform(
            get("$API_PREFIX/files/previews")
                .param("path", uploadFileResponse.path)
        ).andExpectAll(
            status().isOk,
            jsonPath("$").isNotEmpty
        )
    }

    @Test
    fun `findDocumentUrl should return OK`() {
        val uploadFileResponse = uploadDocument()
        mockMvc.perform(
            get("$API_PREFIX/files/urls")
                .param("path", uploadFileResponse.path)
        ).andExpectAll(
            status().isOk,
            jsonPath("$").isNotEmpty
        )
    }

    private fun uploadDocument(): UploadFileResponse {
        val file = MockMultipartFile(
            "file",
            "document.png",
            MediaType.IMAGE_PNG_VALUE,
            javaClass.getResourceAsStream("/document.png")!!
        )
        return mockMvc.perform(
            multipart("$API_PREFIX/files").file(file)
        ).andExpect(status().isCreated).andReturn().response.contentAsString.toObject()
    }
}