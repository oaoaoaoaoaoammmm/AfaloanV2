package com.example.afaloan.exceptions

import com.example.afaloan.exceptions.ErrorUtil.asResponseEntity
import com.example.afaloan.utils.logger
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.MultipartException

@ControllerAdvice
class RestControllerAdvice {

    @ApiResponse(responseCode = "500", useReturnTypeSchema = true)
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

    @ExceptionHandler(S3Exception::class)
    fun s3Exception(ex: Exception): ResponseEntity<Error> {
        logger.error(ex) { "Error working with s3" }
        return Error(status = HttpStatus.INTERNAL_SERVER_ERROR.value(), code = ErrorCode.FILE_SERVICE_UNAVAILABLE)
            .asResponseEntity()
    }

    @ExceptionHandler(MultipartException::class)
    fun fileException(exception: Exception): ResponseEntity<Error> {
        logger.error(exception) { "Error with file resolution" }
        return Error(status = HttpStatus.BAD_REQUEST.value(), code = ErrorCode.WRONG_FILE)
            .asResponseEntity()
    }

    @ExceptionHandler(value = [ConstraintViolationException::class, MethodArgumentNotValidException::class])
    fun validationExceptions(exception: Exception): ResponseEntity<Error> {
        logger.error(exception) { "Handle validation error" }
        return Error(status = HttpStatus.BAD_REQUEST.value(), code = ErrorCode.INVALID_REQUEST)
            .asResponseEntity()
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun authorizationDeniedException(ex: AccessDeniedException): ResponseEntity<Error> {
        logger.error(ex) { "Access denied" }
        return Error(status = HttpStatus.FORBIDDEN.value(), code = ErrorCode.FORBIDDEN)
            .asResponseEntity()
    }
}