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

package ch.schulealtendorf.sporttagpsa.controller.rest.participation

import ch.schulealtendorf.psa.dto.participation.ParticipationCommand
import ch.schulealtendorf.psa.dto.status.StatusDto
import ch.schulealtendorf.psa.dto.status.StatusEntry
import ch.schulealtendorf.psa.dto.status.StatusSeverity
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.config.PSAScope
import ch.schulealtendorf.sporttagpsa.controller.config.SecurityRequirementNames
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Participation", description = "Manage the participation")
class ParticipationController(
    private val participationManager: ParticipationManager
) {
    @Operation(
        summary = "Get the participation status",
        tags = ["Participation"],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPATION])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Participation status",
                content = [Content(schema = Schema(implementation = StatusDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participation')")
    @GetMapping("/participation", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipation(): StatusDto {
        val status = participationManager.getParticipationStatus()

        return StatusDto(
            StatusSeverity.OK,
            listOf(
                StatusEntry(
                    StatusSeverity.INFO,
                    status
                )
            )
        )
    }

    @Operation(
        summary = "Close or reset the participation",
        tags = ["Participation"],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPATION])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Closed or reset the participation"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participation') and hasRole('ADMIN')")
    @PatchMapping("/participation")
    fun updateParticipation(@RequestBody command: ParticipationCommand) {
        when (command) {
            ParticipationCommand.CLOSE -> participationManager.closeParticipation()
            ParticipationCommand.RESET -> participationManager.resetParticipation()
        }
    }

    @Operation(
        summary = "List all sport types",
        tags = ["Participation"],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.SPORT_READ])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "All sport types",
                content = [Content(array = ArraySchema(schema = Schema(implementation = String::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('sport_read')")
    @GetMapping("/sports", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSportTypes(): List<String> {
        return participationManager.getSportTypes()
    }
}
