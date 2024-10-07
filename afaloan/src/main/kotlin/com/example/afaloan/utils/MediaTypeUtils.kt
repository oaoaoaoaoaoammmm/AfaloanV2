package com.example.afaloan.utils

import org.springframework.http.MediaType

private const val APPLICATION_DOC_TYPE = "application/msword"
private const val APPLICATION_DOCX_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"

val AVAILABLE_DOCUMENT_TYPES = listOf(
    MediaType.APPLICATION_PDF_VALUE,
    MediaType.IMAGE_JPEG_VALUE,
    MediaType.IMAGE_PNG_VALUE,
    APPLICATION_DOCX_TYPE,
    APPLICATION_DOC_TYPE
)