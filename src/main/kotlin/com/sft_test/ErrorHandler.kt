package com.sft_test

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class ErrorHandler : ResponseEntityExceptionHandler() {

    override fun handleHttpMessageNotReadable(
            ex: HttpMessageNotReadableException,
            headers: HttpHeaders,
            status: HttpStatus,
            request: WebRequest
    ): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(HttpStatus.BAD_REQUEST, "Malformed JSON request"))
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }

    @ExceptionHandler(WidgetNotFoundException::class)
    protected fun handleWidgetNotFoundException(e: WidgetNotFoundException): ResponseEntity<Any> {
        return buildResponseEntity(ApiError(
                status = HttpStatus.NOT_FOUND,
                message = e.localizedMessage
        ))
    }
}
