package com.rangjin.kotlinblog.global.error

import com.rangjin.kotlinblog.global.error.dto.ExceptionResponseDto
import com.rangjin.kotlinblog.global.error.dto.ValidationErrorResponseDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.servlet.http.HttpServletRequest

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(CustomException::class)
    protected fun handlerBaseException(
        e: CustomException,
        request: HttpServletRequest
    ): ResponseEntity<ExceptionResponseDto> {
        return ResponseEntity.status(e.errorCode.status).body(ExceptionResponseDto(e.errorCode, request.requestURI))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun validationException(
        e: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ValidationErrorResponseDto> {
        val field = e.bindingResult.fieldError!!.field
        val message = e.bindingResult.fieldError!!.defaultMessage

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ValidationErrorResponseDto(HttpStatus.BAD_REQUEST, field, message!!, request.requestURI))
    }

}