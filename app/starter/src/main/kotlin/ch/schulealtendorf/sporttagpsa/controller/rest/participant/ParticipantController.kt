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

package ch.schulealtendorf.sporttagpsa.controller.rest.participant

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.ParticipationStatusDto
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import ch.schulealtendorf.sporttagpsa.controller.rest.NotFoundException
import ch.schulealtendorf.sporttagpsa.controller.rest.RestParticipant
import ch.schulealtendorf.sporttagpsa.controller.rest.json
import javax.validation.Valid
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
@RequestMapping("/api/rest")
class ParticipantController(
    private val participantManager: ParticipantManager,
    private val participationManager: ParticipationManager,
    private val groupManager: GroupManager
) {

    companion object {
        const val PARTICIPANT: String = "/participant/{participant_id}"
    }

    @PreAuthorize("#oauth2.hasScope('participant_read')")
    @GetMapping("/participants", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipants(@RequestParam("group", required = false) groupName: String?): List<RestParticipant> {

        return participantManager.getParticipants()
            .filter { (groupName == null) || it.group.name == groupName }
            .map { json(it) }
    }

    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PostMapping("/participants")
    fun createParticipant(
        @RequestParam(
            "group",
            required = true
        ) groupName: String,
        @Valid @RequestBody participant: CreateParticipant
    ) {

        val group = groupManager.getGroup(groupName)
            .orElseThrow { BadRequestException("Could not find group: name=$groupName") }

        var newParticipant = ParticipantDto(
            0,
            participant.surname,
            participant.prename,
            participant.gender,
            BirthdayDto(participant.birthday),
            false,
            participant.address,
            participant.town,
            group
        )

        newParticipant = participantManager.saveParticipant(newParticipant)

        val participationStatus = participationManager.getParticipationStatus()

        if (participationStatus == ParticipationStatusDto.OPEN) {
            participationManager.participate(newParticipant, participant.sport)
        } else if (participationStatus == ParticipationStatusDto.CLOSE) {
            participationManager.reParticipate(newParticipant, participant.sport)
        }
    }

    @PreAuthorize("#oauth2.hasScope('participant_read')")
    @GetMapping(PARTICIPANT, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipant(@PathVariable("participant_id") id: Int): RestParticipant {

        val participant = participantManager.getParticipantById(id)

        return json(participant)
    }

    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PatchMapping(PARTICIPANT)
    fun patchParticipant(@PathVariable("participant_id") id: Int, @RequestBody patchParticipant: UpdateParticipant) {

        var participant = participantManager.getParticipantById(id)

        participant = participant
            .copy(surname = patchParticipant.surname ?: participant.surname)
            .copy(prename = patchParticipant.prename ?: participant.prename)
            .copy(gender = patchParticipant.gender ?: participant.gender)
            .copy(birthday = patchParticipant.birthday.toBirthday() ?: participant.birthday)
            .copy(address = patchParticipant.address ?: participant.address)

        participant = participantManager.saveParticipant(participant)

        if (patchParticipant.absent == true) {
            participationManager.markAsAbsent(participant)
        } else if (patchParticipant.absent == false) {
            participationManager.markAsPresent(participant)
        }
    }

    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @PutMapping(PARTICIPANT)
    fun putParticipantTown(@PathVariable("participant_id") id: Int, @RequestBody patchParticipant: UpdateParticipant) {

        var participant = participantManager.getParticipantById(id)

        participant = participant
            .copy(town = patchParticipant.town ?: participant.town)

        participant = participantManager.saveParticipant(participant)

        if (patchParticipant.sport == null) {
            return
        }

        val participationStatus = participationManager.getParticipationStatus()

        if (participationStatus == ParticipationStatusDto.OPEN) {
            participationManager.participate(participant, patchParticipant.sport)
        } else if (participationStatus == ParticipationStatusDto.CLOSE) {
            participationManager.reParticipate(participant, patchParticipant.sport)
        }
    }

    @PreAuthorize("#oauth2.hasScope('participant_write')")
    @DeleteMapping(PARTICIPANT)
    fun deleteParticipant(@PathVariable("participant_id") id: Int) {

        val paritcipant = participantManager.getParticipant(id)

        paritcipant.ifPresent {
            participantManager.deleteParticipant(it)
        }
    }

    private fun ParticipantManager.getParticipantById(id: Int): ParticipantDto {
        return getParticipant(id)
            .orElseThrow { NotFoundException("Could not found participant: id=$id") }
    }

    private fun Long?.toBirthday() = if (this == null) null else BirthdayDto(this)
}
