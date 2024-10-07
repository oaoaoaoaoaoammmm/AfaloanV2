package com.example.afaloan.services

import com.example.afaloan.configurations.generators.IdGenerator
import com.example.afaloan.exceptions.ErrorCode
import com.example.afaloan.exceptions.InternalException
import com.example.afaloan.services.api.S3Api
import com.example.afaloan.utils.SecurityContext
import com.example.afaloan.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileService(
    private val s3Api: S3Api,
    private val idGenerator: IdGenerator
) {

    fun download(path: String, userId: String): ByteArray {
        if (!path.contains(userId)) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.DOCUMENT_NOT_YOURS)
        }
        logger.info { "Finding document by path - $path" }
        return s3Api.download(path)
    }

    fun findObjectsPreviewsByUser(userId: String): List<String> {
        logger.info { "Finding document's previews by user - $userId" }
        return s3Api.findObjectsPreviews(userId)
    }

    fun findObjectUrl(path: String, userId: String): String {
        if (!path.contains(userId)) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.DOCUMENT_NOT_YOURS)
        }
        logger.info { "Finding document's url by path - $path" }
        return s3Api.findPresignedObject(path)
    }

    fun upload(file: MultipartFile, availableDocumentType: List<String>): String {
        if (availableDocumentType.isEmpty() || file.contentType !in availableDocumentType) {
            throw InternalException(httpStatus = HttpStatus.BAD_REQUEST, errorCode = ErrorCode.UNAVAILABLE_CONTENT_TYPE)
        }
        val userId = SecurityContext.getAuthorizedUserId().toString()
        val path = s3Api.upload(
            path = "$userId/${idGenerator.generateId()}/${file.originalFilename}",
            inputStream = file.inputStream
        )
        logger.info { "Document saved successfully by user with id - $userId" }
        return path
    }

    fun delete(path: String) {
        val userId = SecurityContext.getAuthorizedUserId().toString()
        if (!path.contains(userId)) {
            throw InternalException(httpStatus = HttpStatus.FORBIDDEN, errorCode = ErrorCode.DOCUMENT_NOT_YOURS)
        }
        logger.info { "Deleting document by path - $path" }
        s3Api.delete(path)
    }
}