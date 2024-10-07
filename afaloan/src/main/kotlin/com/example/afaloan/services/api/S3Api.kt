package com.example.afaloan.services.api

import com.example.afaloan.configurations.s3.S3Properties
import com.example.afaloan.exceptions.S3Exception
import com.example.afaloan.utils.logger
import io.minio.GetObjectArgs
import io.minio.GetPresignedObjectUrlArgs
import io.minio.ListObjectsArgs
import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.http.Method
import org.springframework.stereotype.Service
import java.io.InputStream
import java.util.concurrent.TimeUnit

@Service
class S3Api(
    private val minioClient: MinioClient,
    private val s3Properties: S3Properties
) {

    fun upload(path: String, inputStream: InputStream): String {
        try {
            val putObjectArgs = PutObjectArgs.builder()
                .bucket(s3Properties.bucket)
                .`object`(path)
                .stream(inputStream, inputStream.available().toLong(), PART_SIZE)
                .build()
            minioClient.putObject(putObjectArgs)
        } catch (ex: Exception) {
            throw S3Exception(ex, "Can't upload object")
        }
        logger.trace { "Successfully put object to path - $path" }
        return path
    }

    fun download(path: String): ByteArray {
        return try {
            val getObjectArgs = GetObjectArgs.builder()
                .bucket(s3Properties.bucket)
                .`object`(path)
                .build()
            minioClient.getObject(getObjectArgs).readBytes()
        } catch (ex: Exception) {
            throw S3Exception(ex, "Can't download object")
        }
    }

    fun findObjectsPreviews(prefix: String): List<String> {
        return try {
            val listObjectsArgs = ListObjectsArgs.builder()
                .bucket(s3Properties.bucket)
                .prefix(prefix)
                .build()
            minioClient.listObjects(listObjectsArgs)
                .map { it.get().objectName() }
                .toList()
        } catch (ex: Exception) {
            throw S3Exception(ex, "Can't find object's list")
        }
    }

    fun findPresignedObject(path: String): String {
        return try {
            val getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(s3Properties.bucket)
                .`object`(path)
                .method(Method.GET)
                .expiry(EXPIRY, TimeUnit.HOURS)
                .build()
            minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs)
        } catch (ex: Exception) {
            throw S3Exception(ex, "Can't find object's url")
        }
    }

    fun delete(path: String) {
        try {
            val removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(s3Properties.bucket)
                .`object`(path)
                .build()
            minioClient.removeObject(removeObjectArgs)
        } catch (ex: Exception) {
            throw S3Exception(ex, "Can't delete object")
        }
        logger.trace { "Successfully deleted objects by path - $path" }
    }

    private companion object {
        const val EXPIRY = 1
        const val PART_SIZE = -1L
    }
}