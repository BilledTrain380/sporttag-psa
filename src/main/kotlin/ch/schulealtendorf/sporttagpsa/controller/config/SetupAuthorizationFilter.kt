/*
 * Copyright (c) 2018 by Nicolas Märchy
 *
 * This file is part of Sporttag PSA.
 *
 * Sporttag PSA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sporttag PSA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sporttag PSA.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Sporttag PSA.
 *
 * Sporttag PSA ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Sporttag PSA wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 *
 */

package ch.schulealtendorf.sporttagpsa.controller.config

import ch.schulealtendorf.sporttagpsa.business.setup.SetupManager
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Request filter to redirect, deny or allow the request to the setup page.
 *
 * This filter will redirect to the setup page if PSA is not initialized yet.
 * This filter will allow to access the setup page if PSA is not initialized yet.
 * This filter will deny to access the setup page if PSA is already initialized.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
class SetupAuthorizationFilter(
        private val setupManager: SetupManager
): OncePerRequestFilter() {

    /**
     * Checks if the setup page can be accessed, if it is forbidden or if it should redirect to it.
     *
     * Common static resource locations are always allowed to access.
     * The common static resource locations are defined by {@link PathRequest#toStaticResources#atCommonLocations}.
     */
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        if (request.servletPath == "/setup" && setupManager.isInitialized.not()) {
            filterChain.doFilter(request, response)
            return
        }

        if (request.servletPath == "/setup" && setupManager.isInitialized) {
            response.sendError(403, "Your are not allowed to access this page!")
            return
        }

        if (PathRequest.toStaticResources().atCommonLocations().matches(request).not() && setupManager.isInitialized.not()) {
            response.sendRedirect("${request.scheme}://${request.serverName}:${request.serverPort}/setup")
            return
        }

        filterChain.doFilter(request, response)
    }
}