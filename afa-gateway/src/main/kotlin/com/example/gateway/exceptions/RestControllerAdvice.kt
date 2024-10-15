package com.example.gateway.exceptions

import com.example.gateway.exceptions.ErrorUtil.asResponseEntity
import com.example.gateway.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class RestControllerAdvice {
    @ExceptionHandler(Exception::class)
    fun defaultHandler(ex: Exception): ResponseEntity<Error> {
        logger.error(ex) { "Handle default error" }
        return Error(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), code = ErrorCode.SERVICE_UNAVAILABLE)
            .asResponseEntity()
    }

    @ExceptionHandler(InternalException::class)
    fun internalException(ex: InternalException): ResponseEntity<Error> {
        logger.error(ex) { "Internal error" }
        return Error(status = ex.httpStatus.value(), code = ex.errorCode)
            .asResponseEntity()
    }
}