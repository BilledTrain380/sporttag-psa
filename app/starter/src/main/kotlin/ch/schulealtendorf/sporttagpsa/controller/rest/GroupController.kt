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

package ch.schulealtendorf.sporttagpsa.controller.rest

import ch.schulealtendorf.psa.dto.group.GroupStatusType
import ch.schulealtendorf.psa.dto.group.OverviewGroupDto
import ch.schulealtendorf.psa.dto.group.SimpleGroupDto
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
    private val groupManager: GroupManager
) {
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
}