package com.example.afaloan.services.api

import com.example.afaloan.configurations.s3.S3Properties
import com.example.afaloan.exceptions.S3Exception
import io.minio.MinioClient
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.IOException

class S3ApiTest {

    private val minioClient = mock<MinioClient>()
    private val s3Properties = mock<S3Properties>()
    private val s3Api = S3Api(minioClient, s3Properties)

    @Test
    fun `upload should execute successfully`() {
        val path = "userId/uuid/document.png"
        val inputStream = javaClass.getResourceAsStream("/document.png")!!
        whenever(s3Properties.bucket).thenReturn("afaloan")

        val result = s3Api.upload(path, inputStream)

        assertThat(path).isEqualTo(result)
    }

    @Test
    fun `upload should throw exception`() {
        val path = "userId/uuid/document.png"
        val inputStream = javaClass.getResourceAsStream("/document.png")!!
        whenever(minioClient.putObject(any())).thenThrow(IOException::class.java)

        val s3Exception = assertThrows<S3Exception> { s3Api.upload(path, inputStream) }

        assertThat(s3Exception).isNotNull()
    }

    @Test
    fun `download should throw exception`() {
        val path = "userId/uuid/document.png"
        whenever(minioClient.getObject(any())).thenThrow(IOException::class.java)

        val s3Exception = assertThrows<S3Exception> { s3Api.download(path) }

        assertThat(s3Exception).isNotNull()
    }

    @Test
    fun `findObjectsPreviews should throw exception`() {
        val path = "userId"
        whenever(minioClient.listObjects(any())).thenAnswer { throw IOException() }

        val s3Exception = assertThrows<S3Exception> { s3Api.findObjectsPreviews(path) }

        assertThat(s3Exception).isNotNull()
    }

    @Test
    fun `findPresignedObject should throw exception`() {
        val path = "userId"
        whenever(minioClient.getPresignedObjectUrl(any())).thenThrow(IOException::class.java)

        val s3Exception = assertThrows<S3Exception> { s3Api.findPresignedObject(path) }

        assertThat(s3Exception).isNotNull()
    }

    @Test
    fun `delete should throw exception`() {
        val path = "userId"
        whenever(minioClient.removeObject(any())).thenThrow(IOException::class.java)

        val s3Exception = assertThrows<S3Exception> { s3Api.delete(path) }

        assertThat(s3Exception).isNotNull()
    }
}