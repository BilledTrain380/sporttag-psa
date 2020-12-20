package ch.schulealtendorf.psa.core.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception for 404 Not Found status code.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class NotFoundException(message: String?) : RuntimeException(message)
