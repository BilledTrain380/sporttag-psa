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

import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationManager
import ch.schulealtendorf.sporttagpsa.controller.rest.*
import ch.schulealtendorf.sporttagpsa.model.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * Rest controller for the participants.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
class ParticipantController(
        private val participantManager: ParticipantManager,
        private val participationManager: ParticipationManager,
        private val groupManager: GroupManager
) {

    companion object {
        const val PARTICIPANT: String = "/participant/{participant_id}"
    }

    @GetMapping(PARTICIPANT, produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipant(@PathVariable("participant_id") id: Int): RestParticipant {

        val participant = participantManager.getParticipantByid(id)

        return participant.toRest()
    }

    @PatchMapping(PARTICIPANT)
    fun patchParticipant(@PathVariable("participant_id") id: Int, @RequestBody patchParticipant: PatchParticipant) {

        var participant = participantManager.getParticipantByid(id)

        participant = participant
                .copy(surname = patchParticipant.surname ?: participant.surname )
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

    @PutMapping(PARTICIPANT)
    fun putParticipantTown(@PathVariable("participant_id") id: Int, @RequestBody patchParticipant: PatchParticipant) {

        var participant = participantManager.getParticipantByid(id)

        participant = participant
                .copy(town = patchParticipant.town.toTown() ?: participant.town)

        participant = participantManager.saveParticipant(participant)

        if (patchParticipant.sport != null) {
            participationManager.participate(participant, Sport(patchParticipant.sport.name))
        }

    }

    @GetMapping("/participation", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getParticipation(): RestParticipationStatus {
        val status = participationManager.getParticipationStatus()
        return RestParticipationStatus(status)
    }

    @PatchMapping("/participation")
    fun updateParticipation(@RequestBody participation: RestParticipationStatus) {

        when (participation.status) {
            ParticipationStatus.OPEN -> throw ForbiddenException("Participation can not be set to OPEN. Use RESET to reopen a participation")
            ParticipationStatus.CLOSE -> participationManager.closeParticipation()
            ParticipationStatus.RESET -> participationManager.resetParticipation()
        }
    }

    private fun ParticipantManager.getParticipantByid(id: Int): Participant {
        return getParticipant(id)
                .orElseThrow { NotFoundException("Could not found participant: id=$id") }
    }

    private fun Long?.toBirthday() = if(this == null) null else Birthday(this)

    private fun RestTown?.toTown() = if (this == null) null else Town(id, zip, name)

    private fun Participant.toRest(): RestParticipant {
        return RestParticipant(
                id,
                surname,
                prename,
                gender,
                birthday.milliseconds,
                absent,
                address,
                town.toRest(),
                group.toRest(),
                sport.map { it.toRest() }.orElseGet { null }

        )
    }

    private fun Town.toRest(): RestTown {
        return RestTown(
                id,
                zip,
                name
        )
    }

    private fun Group.toRest(): RestGroup {
        return RestGroup(
                name,
                coach.name,
                groupManager.hasPendingParticipation(this)
        )
    }

    private fun Sport.toRest() = RestSport(name)
}