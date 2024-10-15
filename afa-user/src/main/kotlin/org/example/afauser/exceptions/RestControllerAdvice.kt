package org.example.afauser.exceptions

import io.jsonwebtoken.JwtException
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.ConstraintViolationException
import org.example.afauser.exceptions.ErrorUtil.asResponseEntity
import org.example.afauser.utils.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

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

    @ExceptionHandler(JwtException::class)
    fun jwtException(ex: JwtException): ResponseEntity<Error> {
        logger.error(ex) { "JWT error" }
        return Error(status = HttpStatus.BAD_REQUEST.value(), code = ErrorCode.TOKEN_INCORRECT_FORMAT)
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