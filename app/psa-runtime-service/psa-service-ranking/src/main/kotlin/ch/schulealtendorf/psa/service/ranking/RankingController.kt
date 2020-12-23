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

package ch.schulealtendorf.psa.service.ranking

import ch.schulealtendorf.psa.core.web.BadRequestException
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import ch.schulealtendorf.psa.dto.ranking.RankingDataDto
import ch.schulealtendorf.psa.service.ranking.business.RankingExport
import ch.schulealtendorf.psa.service.ranking.business.RankingExportManager
import ch.schulealtendorf.psa.service.ranking.business.reporter.DisciplineExport
import ch.schulealtendorf.psa.service.standard.web.buildFileResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api")
@Tag(name = "Ranking", description = "Manage the ranking")
@SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.RANKING])
class RankingController(
    private val exportManager: RankingExportManager,
) {
    private val log = KotlinLogging.logger {}

    @Operation(
        summary = "Download the ranking as zip file",
        tags = ["Ranking"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "A zip containing the ranking files",
                content = [Content(mediaType = "application/octet-stream")]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('ranking')")
    @PostMapping(
        "/ranking/download",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun createRanking(@RequestBody data: RankingDataDto): ResponseEntity<InputStreamResource> {
        log.info { "Create ranking reports" }

        val disciplineExports = data.disciplines.map {
            val discipline = exportManager.getDiscipline(it.discipline)
                .orElseThrow { BadRequestException("The given discipline does not exist: name=${it.discipline}") }

            return@map DisciplineExport(discipline, it.gender)
        }

        val rankingExport = RankingExport(
            disciplineExports,
            data.triathlon,
            data.total,
            data.ubsCup
        )

        val zip = exportManager.generateArchive(rankingExport)

        return buildFileResponse(zip)
    }
}
