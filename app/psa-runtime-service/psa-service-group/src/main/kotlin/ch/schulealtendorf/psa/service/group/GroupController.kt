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

package ch.schulealtendorf.psa.service.group

import ch.schulealtendorf.psa.core.web.BadRequestException
import ch.schulealtendorf.psa.core.web.NotFoundException
import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.group.OverviewGroupDto
import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.psa.dto.oauth.PSAScope
import ch.schulealtendorf.psa.dto.oauth.SecurityRequirementNames
import ch.schulealtendorf.psa.service.group.business.GroupImportManager
import ch.schulealtendorf.psa.service.group.business.parsing.CSVParsingException
import ch.schulealtendorf.psa.service.group.business.parsing.GroupFileParser
import ch.schulealtendorf.psa.service.standard.manager.GroupManager
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

/**
 * Rest controller for {@link Group}.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Group", description = "Manage groups")
class GroupController(
    private val groupImportManager: GroupImportManager,
    private val groupManager: GroupManager,
    private val fileParser: GroupFileParser
) {
    private val log = KotlinLogging.logger {}

    @Operation(
        summary = "Find groups by name",
        tags = ["Group"],
        parameters = [
            Parameter(
                name = "group_name",
                description = "The group name to find"
            )
        ]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.GROUP_READ])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found group",
                content = [Content(schema = Schema(implementation = SimpleGroupDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Group not found",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('group_read')")
    @GetMapping("/group/{group_name}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroup(@PathVariable("group_name") name: String): SimpleGroupDto {
        return groupManager.getGroup(name)
            .orElseThrow { NotFoundException("Could not find group: name=$name") }
    }

    @Operation(
        summary = "List groups",
        tags = ["Group"]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.GROUP_READ])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Group list",
                content = [Content(array = ArraySchema(schema = Schema(implementation = SimpleGroupDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('group_read')")
    @GetMapping("/groups", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroups(): List<SimpleGroupDto> {
        return groupManager.getGroups()
    }

    @Operation(
        summary = "List group overview",
        tags = ["Group"],
        parameters = [
            Parameter(
                name = "status_type",
                description = "The status type to filter"
            )
        ]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.GROUP_READ])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Group list",
                content = [Content(array = ArraySchema(schema = Schema(implementation = OverviewGroupDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('group_read')")
    @GetMapping("/groups/overview", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getGroups(
        @RequestParam("status_type", required = false) statusType: GroupStatusType?
    ): List<OverviewGroupDto> {
        if (statusType == null) {
            return groupManager.getOverview()
        }

        return groupManager.getOverviewBy(statusType)
    }

    @Operation(
        summary = "Import groups by a csv file",
        tags = ["Group"]
    )
    @SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.GROUP_WRITE])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Imported groups",
                content = [Content(mediaType = MediaType.TEXT_PLAIN_VALUE)]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('group_write')")
    @PostMapping(
        "/groups/import",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.TEXT_PLAIN_VALUE]
    )
    fun importGroup(@RequestParam("group-input") file: MultipartFile): ResponseEntity<String> {
        log.info { "Import groups with csv file: name=${file.name}, size=${file.size}" }

        return try {
            val participants = fileParser.parseCSV(file)
            participants.forEach(groupImportManager::import)

            ResponseEntity.ok("Group import successful")
        } catch (exception: CSVParsingException) {
            // we increment the line, so its not zero based line number for the user
            ResponseEntity(
                "${exception.message} (at line ${exception.line + 1}:${exception.column})",
                HttpStatus.BAD_REQUEST
            )
        } catch (exception: IllegalArgumentException) {
            throw BadRequestException(exception.message)
        }
    }
}
