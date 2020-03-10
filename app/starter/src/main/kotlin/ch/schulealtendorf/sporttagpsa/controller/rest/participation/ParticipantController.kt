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

import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.ParticipantElement
import ch.schulealtendorf.psa.dto.participation.ParticipantInput
import ch.schulealtendorf.psa.dto.participation.ParticipantRelation
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.config.PSAScope
import ch.schulealtendorf.sporttagpsa.controller.config.SecurityRequirementNames
import ch.schulealtendorf.sporttagpsa.controller.rest.NotFoundException
import ch.schulealtendorf.sporttagpsa.lib.ifNotNull
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

/**
 * Rest controller for the participants.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Participant", description = "Manage participants")
class ParticipantController(
    private val participantManager: ParticipantManager,
    private val participationManager: ParticipationManager,
    private val groupManager: GroupManager
) {
    companion object {
        const val PARTICIPANT: String = "/participant/{participant_id}"
    }

    @Operation(
        summary = "List participants",
        tags = ["Participant"],
        parameters = [
            Parameter(
                name = "group",
                description = "Only include participants related to a group"
            )
        ],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_READ])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List participants",
                content = [Content(array = ArraySchema(schema = Schema(implementation = ParticipantDto::class)))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_read')")
    @GetMapping("/participants", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipants(@RequestParam("group", required = false) groupName: String?): List<ParticipantDto> {
        if (groupName == null) {
            return participantManager.getParticipants()
        }

        return participantManager.getParticipantsByGroup(groupName)
    }

    @Operation(
        summary = "Get a participant",
        tags = ["Participant"],
        parameters = [
            Parameter(
                name = "participant_id",
                description = "The id of the participant to find"
            )
        ],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_READ])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Found participant",
                content = [Content(schema = Schema(implementation = ParticipantDto::class))]
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Participant not found",
                content = [Content()]
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_read')")
    @GetMapping(PARTICIPANT, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipant(@PathVariable("participant_id") id: Int): ParticipantDto {
        return participantManager.getParticipantOrElseFail(id)
    }

    @Operation(
        summary = "Add a new participant",
        tags = ["Participant"],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_WRITE])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Participant created"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            ApiResponse(
                responseCode = "400",
                description = "Group does not exist"
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PostMapping("/participants")
    fun createParticipant(@RequestBody input: ParticipantInput) {
        val participant = ParticipantDto(
            surname = input.surname,
            prename = input.prename,
            gender = input.gender,
            birthday = input.birthday,
            address = input.address,
            town = input.town,
            isAbsent = false,
            group = input.group
        )

        participantManager.saveParticipant(participant)
    }

    @Operation(
        summary = "Update participant properties",
        tags = ["Participant"],
        parameters = [
            Parameter(
                name = "participant_id",
                description = "The id of the participant to update"
            )
        ],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_WRITE])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated participant"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Participant not found"
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PatchMapping(PARTICIPANT)
    fun updateParticipant(@PathVariable("participant_id") id: Int, @RequestBody dto: ParticipantElement) {
        val builder = participantManager.getParticipantOrElseFail(id).toBuilder()

        dto.prename.ifNotNull {
            builder.setPrename(it)
        }

        dto.surname.ifNotNull {
            builder.setSurname(it)
        }

        dto.address.ifNotNull {
            builder.setAddress(it)
        }

        dto.birthday.ifNotNull {
            builder.setBirthday(it)
        }

        dto.gender.ifNotNull {
            builder.setGender(it)
        }

        dto.town.ifNotNull {
            builder.setTown(it)
        }

        dto.isAbsent.ifNotNull {
            builder.setAbsent(it)
        }

        participantManager.saveParticipant(builder.build())
    }

    @Operation(
        summary = "Update participant relations",
        tags = ["Participant"],
        parameters = [
            Parameter(
                name = "participant_id",
                description = "The id of the participant to update"
            )
        ],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_WRITE])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Updated participant"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            ),
            ApiResponse(
                responseCode = "404",
                description = "Participant not found"
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PutMapping(PARTICIPANT)
    fun updateParticipant(@PathVariable("participant_id") id: Int, @RequestBody dto: ParticipantRelation) {
        val participant = participantManager.getParticipantOrElseFail(id)

        dto.sportType.ifNotNull {
            val participationStatus = participationManager.getParticipationStatus()

            if (participationStatus == ParticipationStatusType.OPEN) {
                participationManager.participate(participant, it)
            } else if (participationStatus == ParticipationStatusType.CLOSED) {
                participationManager.reParticipate(participant, it)
            }
        }
    }

    @Operation(
        summary = "Delete a participant",
        tags = ["Participant"],
        parameters = [
            Parameter(
                name = "participant_id",
                description = "The id of the participant to delete"
            )
        ],
        security = [SecurityRequirement(name = SecurityRequirementNames.OAUTH2, scopes = [PSAScope.PARTICIPANT_WRITE])]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Deleted the participant"
            ),
            ApiResponse(
                responseCode = "401",
                description = "Unauthorized"
            )
        ]
    )
    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @DeleteMapping(PARTICIPANT)
    fun deleteParticipant(@PathVariable("participant_id") id: Int) {
        participantManager.deleteParticipantById(id)
    }

    private fun ParticipantManager.getParticipantOrElseFail(id: Int): ParticipantDto {
        return getParticipant(id)
            .orElseThrow { NotFoundException("Could not found participant: id=$id") }
    }
}
