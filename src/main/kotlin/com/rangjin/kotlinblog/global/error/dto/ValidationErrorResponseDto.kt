package com.rangjin.kotlinblog.global.error.dto

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

class ValidationErrorResponseDto (
    val time: LocalDateTime,

    val status: HttpStatus,

    val field: String,

    val message: String,

    val requestURI: String,
) {

    constructor(status: HttpStatus, field: String, message: String, requestURI: String): this(
        LocalDateTime.now(), status, field, message, requestURI
    )

}