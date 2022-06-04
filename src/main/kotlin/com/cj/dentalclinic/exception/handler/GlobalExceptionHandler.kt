package com.cj.dentalclinic.exception.handler

import com.cj.dentalclinic.exception.ResourceNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

data class ErrorResponse(val code: HttpStatus, val message: String?)

@RestControllerAdvice
class GlobalExceptionHandler {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(ResourceNotFoundException::class)
  fun handleResourceNotFoundException(exception: ResourceNotFoundException) = ErrorResponse(NOT_FOUND, exception.message)
}
