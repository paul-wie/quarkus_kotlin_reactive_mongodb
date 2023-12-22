package org.myecommerce.exception

import jakarta.ws.rs.core.Response
import java.lang.Exception

open class CustomException(
    val code: Int,
    val errorMessage: String,
): Exception()


class NotFoundException(errorMessage: String) :
    CustomException(code = Response.Status.NOT_FOUND.statusCode, errorMessage = errorMessage)

class DeserializationException(errorMessage: String) :
    CustomException(code = Response.Status.BAD_REQUEST.statusCode, errorMessage = errorMessage)

class ServiceUnavailable() :
    CustomException(code = Response.Status.SERVICE_UNAVAILABLE.statusCode, errorMessage = "Service currently not available")

class BadRequestException() :
    CustomException(code = Response.Status.BAD_REQUEST.statusCode, errorMessage = "Bad request")