package com.example.afaloan.exceptions

import org.springframework.http.HttpStatus

open class InternalException(
    val httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
    val errorCode: ErrorCode = ErrorCode.SERVICE_UNAVAILABLE
) : RuntimeException()