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

package ch.schulealtendorf.psa.service.event

import ch.schulealtendorf.psa.dto.event.EventSheetData
import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.participation.SportDto
import ch.schulealtendorf.psa.service.event.business.EventSheetDisciplineExport
import ch.schulealtendorf.psa.service.event.business.EventSheetExportManager
import ch.schulealtendorf.psa.service.event.business.ParticipantListExportManager
import ch.schulealtendorf.psa.service.event.business.reporter.StartlistReporter
import ch.schulealtendorf.psa.service.standard.disciplineDtoOf
import ch.schulealtendorf.psa.service.standard.exception.web.BadRequestException
import ch.schulealtendorf.psa.service.standard.manager.GroupManager
import ch.schulealtendorf.psa.service.standard.repository.DisciplineRepository
import ch.schulealtendorf.psa.service.standard.web.buildFileResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/event-sheets")
@Tag(name = "EventSheets", description = "Manage event sheets")
class EventSheetController(
    private val eventSheetExportManager: EventSheetExportManager,
    private val participantListExportManager: ParticipantListExportManager,
    private val startlistReporter: StartlistReporter,
    private val disciplineRepository: DisciplineRepository,
    private val groupManager: GroupManager
) {
    @Operation(
        summary = "Get all competitive group names",
        tags = ["EventSheets"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "A list containing all competitive group names",
                content = [Content(array = ArraySchema(schema = Schema(implementation = String::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('event_sheets')")
    @PostMapping("/groups")
    fun getGroupNames(): List<String> {
        return groupManager.getOverviewBy(GroupStatusType.GROUP_TYPE_COMPETITIVE).map { it.group.name }
    }

    @Operation(
        summary = "Download a zip containing the event sheets",
        tags = ["EventSheets"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "A zip containing the event sheets",
                content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('event_sheets')")
    @PostMapping(
        "/download/sheets",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun createEventSheets(@RequestBody dataList: List<EventSheetData>): ResponseEntity<InputStreamResource> {
        val exports = dataList.map { data ->
            val discipline = disciplineRepository.findById(data.discipline)
                .map { disciplineDtoOf(it) }
                .orElseThrow { BadRequestException("The discipline does not exist: name=${data.discipline}") }

            val group = groupManager.getGroup(data.group)
                .orElseThrow { BadRequestException("The group does not exist: name=${data.group}") }

            return@map EventSheetDisciplineExport(discipline, group, data.gender)
        }

        val zip = eventSheetExportManager.generateArchive(exports)
        return buildFileResponse(zip)
    }

    @Operation(
        summary = "Download a zip containing the participation list",
        tags = ["EventSheets"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "A zip containing the participation list",
                content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('event_sheets')")
    @PostMapping(
        "/download/participant-list",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE]
    )
    fun createParticipantList(@RequestBody data: List<SportDto>): ResponseEntity<InputStreamResource> {
        val zip = participantListExportManager.generateArchive(data)
        return buildFileResponse(zip)
    }

    @Operation(
        summary = "Download a zip containing the start list",
        tags = ["EventSheets"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "A zip containing the start list",
                content = [Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participation')")
    @GetMapping("/download/startlist", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    fun createStartList(): ResponseEntity<InputStreamResource> {
        val startList = startlistReporter.generateReport()
        return buildFileResponse(startList)
    }
}
