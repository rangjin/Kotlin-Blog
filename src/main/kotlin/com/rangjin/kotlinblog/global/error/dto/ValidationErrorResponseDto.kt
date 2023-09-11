package com.rangjin.kotlinblog.global.error.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ValidationErrorResponseDto (
    time: LocalDateTime, status: HttpStatus, val field: String, message: String, requestURI: String,
): ExceptionResponseDto(time, status, message, requestURI) {

    constructor(status: HttpStatus, field: String, message: String, requestURI: String): this(
        LocalDateTime.now(), status, field, message, requestURI
    )
}