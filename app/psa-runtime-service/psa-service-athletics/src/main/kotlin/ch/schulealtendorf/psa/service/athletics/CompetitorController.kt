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

package ch.schulealtendorf.psa.service.athletics

import ch.schulealtendorf.psa.core.web.NotFoundException
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.GenderDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultElement
import ch.schulealtendorf.psa.service.athletics.business.CompetitorFilter
import ch.schulealtendorf.psa.service.athletics.business.CompetitorManager
import ch.schulealtendorf.psa.service.athletics.business.CompetitorResultAmend
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 0.0.1
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Competitor", description = "Manage competitors")
class CompetitorController(
    private val competitorManager: CompetitorManager
) {

    @Operation(
        summary = "List competitors",
        tags = ["Competitor"],
        parameters = [
            Parameter(
                name = "group",
                description = "Only include competitors related to a group"
            ),
            Parameter(
                name = "gender",
                description = "Only include competitors matching a gender"
            ),
            Parameter(
                name = "absent",
                description = "If true only absent competitors will be returned, otherwise only present competitors"
            )
        ]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.COMPETITOR_READ])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Competitor list",
                content = [Content(array = ArraySchema(schema = Schema(implementation = CompetitorDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('competitor_read')")
    @GetMapping("/competitors")
    fun getCompetitors(
        @RequestParam("group", required = false) groupName: String?,
        @RequestParam("gender", required = false) gender: GenderDto?,
        @RequestParam("absent", required = false) absent: Boolean?
    ): List<CompetitorDto> {
        val filter = CompetitorFilter(
            group = groupName,
            gender = gender,
            isAbsent = absent
        )

        if (filter.hasAnyFilter()) {
            return competitorManager.getCompetitors(filter)
        }

        return competitorManager.getCompetitors()
    }

    @Operation(
        summary = "Find a competitor by its id",
        tags = ["Competitor"],
        parameters = [
            Parameter(
                name = "competitor_id",
                description = "The competitor id to find"
            )
        ]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.COMPETITOR_READ])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found competitor",
                content = [Content(schema = Schema(implementation = CompetitorDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Not found",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('competitor_read')")
    @GetMapping("/competitor/{competitor_id}")
    fun getCompetitor(@PathVariable("competitor_id") id: Int): CompetitorDto {
        return competitorManager.getCompetitor(id)
            .orElseThrow { NotFoundException("Competitor does not exist: id=$id") }
    }

    @Operation(
        summary = "Update competitor result",
        tags = ["Competitor"],
        parameters = [
            Parameter(
                name = "competitor_id",
                description = "The id of the competitor to update its result"
            )
        ]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.COMPETITOR_WRITE])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated the competitor results",
                content = [Content(array = ArraySchema(schema = Schema(implementation = ResultDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Competitor not found",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('competitor_write')")
    @PutMapping("/competitor/{competitor_id}")
    fun updateCompetitorResult(@PathVariable("competitor_id") id: Int, @RequestBody result: ResultElement): ResultDto {
        try {
            val resultAmend = CompetitorResultAmend(
                competitorId = id,
                result = result
            )

            return competitorManager.updateResult(resultAmend)
        } catch (exception: NoSuchElementException) {
            throw NotFoundException("Could not update result: message=${exception.message}")
        }
    }
}
