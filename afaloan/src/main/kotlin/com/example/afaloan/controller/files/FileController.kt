package com.example.afaloan.controller.files

import com.example.afaloan.controller.files.FileController.Companion.ROOT_URI
import com.example.afaloan.controller.files.dtos.FindFileUrlResponse
import com.example.afaloan.controller.files.dtos.UploadFileResponse
import com.example.afaloan.services.FileService
import com.example.afaloan.utils.AVAILABLE_DOCUMENT_TYPES
import com.example.afaloan.utils.SecurityContext
import com.example.afaloan.utils.logger
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping(ROOT_URI)
class FileController(
    private val fileService: FileService
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestParam file: MultipartFile): UploadFileResponse {
        logger.trace { "Upload document request" }
        val path = fileService.upload(file, AVAILABLE_DOCUMENT_TYPES)
        return UploadFileResponse(path)
    }

    @GetMapping(produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun download(
        @RequestParam path: String,
        @RequestParam(required = false) userId: String = SecurityContext.getAuthorizedUserId().toString()
    ): ResponseEntity<ByteArray> {
        logger.trace { "Download document by path - $path" }
        return fileService.download(path, userId)
            .asFileResponseEntity(path.split("/").last())
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@RequestParam path: String) {
        logger.trace { "Delete document by path - $path" }
        fileService.delete(path)
    }

    @GetMapping(PREVIEWS)
    @ResponseStatus(HttpStatus.OK)
    fun findPreviews(
        @RequestParam(required = false) userId: String = SecurityContext.getAuthorizedUserId().toString()
    ): Collection<String> {
        logger.trace { "Find document previews" }
        val previews = fileService.findObjectsPreviewsByUser(userId)
        logger.trace { "Found previews - $previews" }
        return previews
    }

    @GetMapping(URLS)
    @ResponseStatus(HttpStatus.OK)
    fun findDocumentUrl(
        @RequestParam path: String,
        @RequestParam(required = false) userId: String = SecurityContext.getAuthorizedUserId().toString()
    ): FindFileUrlResponse {
        logger.trace { "Find document url by path - $path" }
        val url = fileService.findObjectUrl(path, userId)
        return FindFileUrlResponse(url)
    }

    private fun ByteArray.asFileResponseEntity(filename: String): ResponseEntity<ByteArray> {
        val contentDisposition: ContentDisposition = ContentDisposition.inline().filename(filename).build()
        val headers = HttpHeaders()
        headers.contentDisposition = contentDisposition
        headers.contentType = MediaType.APPLICATION_OCTET_STREAM
        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(this)
    }

    companion object {
        const val ROOT_URI = "/files"
        const val PREVIEWS = "/previews"
        const val URLS = "/urls"
    }
}