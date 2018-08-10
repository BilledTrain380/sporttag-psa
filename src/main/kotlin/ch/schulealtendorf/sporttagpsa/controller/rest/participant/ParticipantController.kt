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

import ch.schulealtendorf.sporttagpsa.business.clazz.ClassManager
import ch.schulealtendorf.sporttagpsa.business.participation.ParticipantManager
import ch.schulealtendorf.sporttagpsa.controller.rest.*
import ch.schulealtendorf.sporttagpsa.model.*
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

/**
 * Rest controller for the participants.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@RestController
class ParticipantController(
        private val participantManager: ParticipantManager,
        private val classManager: ClassManager
) {

    @GetMapping("/participants", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getAllParticipants(@RequestParam("class", required = false) clazzName: String?): List<RestParticipant> {

        if (clazzName == null) {
            return participantManager.getAllParticipants().map { it.map() }
        }

        val clazz = classManager.getClass(clazzName).orElseThrow { BadRequestException("Could not find class with name '$clazzName'") }

        return participantManager.getAllParticipants(clazz).map { it.map() }
    }

    @PostMapping("/participant")
    fun addParticipant() {
        TODO("This request is not supported yet")
    }

    @GetMapping("/participant/{participant_id}")
    fun getParticipant(@PathVariable("participant_id") participantId: Int): RestParticipant {

        return participantManager.getParticipant(participantId)
                .map { it.map() }
                .orElseThrow { BadRequestException("Could not find participant with id '$participantId'") }
    }

    @PutMapping("/participant/{participant_id}")
    fun updateParticipant(@PathVariable("participant_id") participantId: Int, @Valid @RequestBody restParticipant: RestPutParticipant) {

        val participant = participantManager.getParticipant(participantId)
                .orElseThrow { BadRequestException("Could not find participant with id '$participantId'") }

        val updatedParticipant = participant.copy(
                surname = restParticipant.surname!!,
                prename = restParticipant.prename!!,
                birthday = Birthday(restParticipant.birthday!!),
                address = restParticipant.address!!,
                absent = restParticipant.absent!!
        )

        participantManager.saveParticipant(updatedParticipant)
    }

    @PatchMapping("/participant/{participant_id}")
    fun updateParticipant(@PathVariable("participant_id") participantId: Int, @Valid @RequestBody restParticipant: RestPatchParticipant) {

        if (restParticipant.clazz == null && restParticipant.town == null && restParticipant.sport == null) {
            throw BadRequestException("Missing either 'clazz', 'town' or 'sport' in request body.")
        }

        val participant = participantManager.getParticipant(participantId)
                .orElseThrow { BadRequestException("Could not find participant with id '$participantId'") }

        if (restParticipant.clazz != null) {
            val clazz = classManager.getClass(restParticipant.clazz!!.name!!)
                    .orElseThrow { BadRequestException("Could not find class with name''${restParticipant.clazz!!.name}") }
            participant.update(clazz)
        }

        if (restParticipant.sport != null) {
            participant.update(restParticipant.sport!!)
        }

        if (restParticipant.town != null) {
            participant.update(restParticipant.town!!)
        }
    }

    private fun Participant.update(sport: String) {
        participantManager.saveParticipant(
                this.copy(
                        sport = java.util.Optional.of(sport)
                )
        )
    }

    private fun Participant.update(town: RestTown) {
        participantManager.saveParticipant(
                this.copy(
                        town = town.map()
                )
        )
    }

    private fun Participant.update(clazz: Group) {
        participantManager.saveParticipant(
                this.copy(
                        clazz = clazz
                )
        )
    }

    private fun Participant.map(): RestParticipant {
        return RestParticipant(
                id,
                surname,
                prename,
                true,
                birthday.milliseconds,
                absent,
                address,
                town.map(),
                clazz.map(),
                sport.orElse(null)
        )
    }

    private fun Group.map(): RestClass {
        return RestClass(
                name,
                coach.name,
                classManager.hasPendingParticipation(this)
        )
    }

    private fun Town.map(): RestTown {
        return RestTown(
                id,
                zip,
                name
        )
    }

    private fun RestTown.map(): Town {
        return Town(
                id!!,
                zip!!,
                name!!
        )
    }
}