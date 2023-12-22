package org.myecommerce.exception

import jakarta.ws.rs.core.Response
import jakarta.ws.rs.ext.ExceptionMapper
import jakarta.ws.rs.ext.Provider
@Provider
class CustomExceptionMapper: ExceptionMapper<CustomException> {
    override fun toResponse(exception: CustomException): Response {
        return Response.status(exception.code).entity(exception.errorMessage).build()
    }
}

