package com.rangjin.kotlinblog.global.error.dto

import com.rangjin.kotlinblog.global.error.ErrorCode
import org.springframework.http.HttpStatus
import java.time.LocalDateTime

open class ExceptionResponseDto(
    val time: LocalDateTime,

    val status: HttpStatus,

    val message: String,

    val requestURI: String,
) {

    constructor(errorCode: ErrorCode, requestURI: String): this(
        LocalDateTime.now(), errorCode.status, errorCode.message, requestURI
    )

}