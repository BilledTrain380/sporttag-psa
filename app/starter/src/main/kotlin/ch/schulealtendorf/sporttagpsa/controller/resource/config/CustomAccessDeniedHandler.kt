package ch.schulealtendorf.sporttagpsa.controller.resource.config

import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Custom access denied handler which will return 404 instead of 403.
 */
class CustomAccessDeniedHandler : AccessDeniedHandler {
    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        if (response!!.isCommitted.not()) {
            response.sendError(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.reasonPhrase
            )
        }
    }
}
