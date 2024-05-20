package com.fiap.stock.application.adapter.controller.configuration

import com.fiap.stock.application.domain.errors.ErrorType
import com.fiap.stock.application.domain.errors.SelfOrderManagementException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(SelfOrderManagementException::class)
    protected fun domainErrorHandler(domainException: SelfOrderManagementException): ResponseEntity<ApiError> {
        val apiErrorResponseEntity: ApiErrorResponseEntity =
            when (domainException.errorType) {
                ErrorType.STOCK_ALREADY_EXISTS,
                ErrorType.INSUFFICIENT_STOCK,
                ->
                    ApiErrorResponseEntity(
                        ApiError(domainException.errorType.name, domainException.message),
                        HttpStatus.UNPROCESSABLE_ENTITY,
                    )

                ErrorType.PRODUCT_NOT_FOUND,
                ErrorType.COMPONENT_NOT_FOUND,
                ErrorType.STOCK_NOT_FOUND,
                ->
                    ApiErrorResponseEntity(
                        ApiError(domainException.errorType.name, domainException.message),
                        HttpStatus.NOT_FOUND,
                    )

                ErrorType.INVALID_PRODUCT_CATEGORY,
                ErrorType.PRODUCT_NUMBER_IS_MANDATORY,
                ErrorType.COMPONENT_NUMBER_IS_MANDATORY,
                ->
                    ApiErrorResponseEntity(
                        ApiError(domainException.errorType.name, domainException.message),
                        HttpStatus.BAD_REQUEST,
                    )

                else ->
                    ApiErrorResponseEntity(
                        ApiError(ErrorType.UNEXPECTED_ERROR.name, domainException.localizedMessage),
                        HttpStatus.INTERNAL_SERVER_ERROR,
                    )
            }
        return ResponseEntity.status(apiErrorResponseEntity.status).body(apiErrorResponseEntity.body)
    }

    data class ApiError(val error: String, val message: String?)

    data class ApiErrorResponseEntity(val body: ApiError, val status: HttpStatus)
}
