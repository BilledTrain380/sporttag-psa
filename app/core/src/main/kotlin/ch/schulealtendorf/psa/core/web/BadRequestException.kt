package ch.schulealtendorf.psa.core.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception for 400 Bad Request status code.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(message: String?) : RuntimeException(message)
