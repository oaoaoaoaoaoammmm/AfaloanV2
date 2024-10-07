package com.example.afaloan.exceptions

class S3Exception(
    override val cause: Throwable,
    override val message: String
) : RuntimeException(message, cause)