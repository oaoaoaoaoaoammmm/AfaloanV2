package com.example.afaloan.services

import com.example.afaloan.configurations.generators.RandomUUIDGenerator
import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.services.api.S3Api
import com.example.afaloan.utils.USER
import com.example.afaloan.utils.mockSecurityContext
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.core.context.SecurityContextHolder

class FileServiceTest {

    private val s3Api = mock<S3Api>()
    private val idGenerator = RandomUUIDGenerator()
    private val fileService = FileService(s3Api, idGenerator)

    @BeforeEach
    fun setUp() = mockSecurityContext()

    @AfterEach
    fun tearDown() = SecurityContextHolder.clearContext()

    @Test
    fun `download should execute successfully`() {
        val path = "${USER.id}/uuid/document.png"
        val byteArray = javaClass.getResourceAsStream("/document.png")!!.readAllBytes()
        whenever(s3Api.download(any())).thenReturn(byteArray)

        val result = fileService.download(path, USER.id.toString())

        assertThat(byteArray).isEqualTo(result)
    }

    @Test
    fun `download should throw FORBIDDEN`() {
        val path = "userId/uuid/document.png"

        val internalException = assertThrows<InternalException> { fileService.download(path, USER.id.toString()) }

        assertThat(internalException.httpStatus).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(internalException.errorCode).isEqualTo(ErrorCode.DOCUMENT_NOT_YOURS)
    }

    @Test
    fun `findObjectsPreviewsByUser should execute successfully`() {
        val previews = listOf("doc1", "doc2")
        whenever(s3Api.findObjectsPreviews(any())).thenReturn(previews)

        val result = fileService.findObjectsPreviewsByUser(USER.id.toString())

        assertThat(previews.size).isEqualTo(result.size)
    }

    @Test
    fun `findObjectUrl should execute successfully`() {
        val url = "url"
        val path = "${USER.id}/uuid/document.png"
        whenever(s3Api.findPresignedObject(any())).thenReturn(url)

        val result = fileService.findObjectUrl(path, USER.id.toString())

        assertThat(result).isEqualTo(url)
    }

    @Test
    fun `findObjectUrl should throw FORBIDDEN`() {
        val path = "userId/uuid/document.png"

        val internalException = assertThrows<InternalException> { fileService.findObjectUrl(path, USER.id.toString()) }

        assertThat(internalException.httpStatus).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(internalException.errorCode).isEqualTo(ErrorCode.DOCUMENT_NOT_YOURS)
    }

    @Test
    fun `upload should execute successfully`() {
        val path = "${USER.id}/uuid/document.png"
        val availableDocumentType = listOf(MediaType.IMAGE_PNG_VALUE)
        val file = MockMultipartFile(
            "file",
            "document.png",
            MediaType.IMAGE_PNG_VALUE,
            javaClass.getResourceAsStream("/document.png")!!
        )
        whenever(s3Api.upload(any(), any())).thenReturn(path)

        val result = fileService.upload(file, availableDocumentType)

        assertThat(result.startsWith("${USER.id}"))
        assertThat(result.endsWith("document.png"))
    }

    @Test
    fun `upload should throw UNAVAILABLE_CONTENT_TYPE`() {
        val availableDocumentType = emptyList<String>()
        val file = MockMultipartFile(
            "file",
            "document.png",
            MediaType.IMAGE_PNG_VALUE,
            javaClass.getResourceAsStream("/document.png")!!
        )

        val internalException = assertThrows<InternalException> { fileService.upload(file, availableDocumentType) }

        assertThat(internalException.httpStatus).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(internalException.errorCode).isEqualTo(ErrorCode.UNAVAILABLE_CONTENT_TYPE)
    }

    @Test
    fun `delete should execute successfully`() {
        val path = "${USER.id}/uuid/document.png"

        assertDoesNotThrow { fileService.delete(path) }
    }

    @Test
    fun `delete should throw FORBIDDEN`() {
        val path = "userId/uuid/document.png"

        val internalException = assertThrows<InternalException> { fileService.delete(path) }

        assertThat(internalException.httpStatus).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(internalException.errorCode).isEqualTo(ErrorCode.DOCUMENT_NOT_YOURS)
    }
}