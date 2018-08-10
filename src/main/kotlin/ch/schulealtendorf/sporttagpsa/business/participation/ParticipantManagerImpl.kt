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

package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.sporttagpsa.business.clazz.ClassManager
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Default implementation of a {@link ParticipantManager} which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class ParticipantManagerImpl(
        private val competitorRepository: ParticipantRepository,
        private val classManager: ClassManager,
        private val absentManager: AbsentManager
): ParticipantManager {

    /**
     * @return all participants
     */
    override fun getAllParticipants(): List<Participant> {
        return competitorRepository.findAll()
                .map { it.map() }
    }

    /**
     * Get all participant related to the given {@code clazz}.
     *
     * @param clazz the class to filter the participants
     *
     * @return all participant related to the given {@code clazz}.
     */
    override fun getAllParticipants(clazz: Group): List<Participant> {
        return competitorRepository.findByGroupName(clazz.name)
                .map { it.map() }
    }

    /**
     * Returns an Optional containing the participant matching
     * the given {@code participantId} or an empty Optional if
     * no participant could be found.
     *
     * @param participantId the id of the participant
     *
     * @return the resulting participant
     */
    override fun getParticipant(participantId: Int): Optional<Participant> {

        val participant = competitorRepository.findById(participantId)

        return participant.map { it.map() }
    }

    /**
     * Saves the given {@code participant}.
     * If the participant does not exists already, a new one is created.
     *
     * The {@code participant#id} property can not be updated.
     *
     * Any given relation is cascaded.
     *
     * @param participant the participant to update
     */
    override fun saveParticipant(participant: Participant) {

        val competitor = competitorRepository.findById(participant.id).orElseGet { ParticipantEntity() }

        competitor.apply {
            surname = participant.surname
            prename = participant.prename
            gender = participant.gender.name
            birthday = participant.birthday.milliseconds
            address = participant.address
            town = TownEntity(participant.town.id, participant.town.zip, participant.town.name)
            group = GroupEntity(participant.clazz.name, CoachEntity(participant.clazz.coach.id, participant.clazz.coach.name))
            participant.sport.ifPresent {
                sport = SportEntity(participant.sport.get())
            }
        }

        competitorRepository.save(competitor)

        if (participant.absent) {
            absentManager.markAsAbsent(competitor)
        } else {
            absentManager.markAsPresent(competitor)
        }
    }

    private fun ParticipantEntity.map(): Participant {

        val clazz = classManager.getClass(group.name)
        val sport: String? = if (sport == null) null else sport?.name

        return Participant(
                id!!,
                surname,
                prename,
                Gender.FEMALE,
                Birthday(birthday),
                absentManager.isAbsent(this),
                address,
                Town(town.id!!, town.zip, town.name),
                clazz.get(),
                Optional.ofNullable(sport)
        )
    }
}
