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

package ch.schulealtendorf.sporttagpsa.business.participant

import ch.schulealtendorf.sporttagpsa.business.clazz.ClassManager
import ch.schulealtendorf.sporttagpsa.business.participation.AbsentManager
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.model.Birthday
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.model.Participant
import ch.schulealtendorf.sporttagpsa.model.Town
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

/**
 * Default implementation of a {@link ParticipantManager} which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class DefaultParticipantManager(
        private val competitorRepository: CompetitorRepository,
        private val classManager: ClassManager,
        private val absentManager: AbsentManager
): ParticipantManager {

    /**
     * @return all participants
     */
    override fun getAllParticipants(): List<Participant> {
        return competitorRepository.findAll()
                .mapNotNull { it?.map() }
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

        return participant.map { it?.map() }
    }

    /**
     * Updates the given {@code participant}.
     *
     * @param participant the participant to update
     *
     * @throws NoSuchElementException if the given {@code participant} or any of its relations could not be found
     */
    override fun updateParticipant(participant: Participant) {

        val competitor = competitorRepository.findById(participant.id).get()

        competitor.apply {
            id = participant.id
            surname = participant.surname
            prename = participant.prename
            gender = participant.gender.value
            address = participant.address
            birthday = participant.birthday.milliseconds
        }

        competitorRepository.save(competitor)
    }

    private fun CompetitorEntity.map(): Participant {

        val clazz = classManager.getClass(clazz.name)
        val sport: String? = if (sport == null) null else sport?.name

        return Participant(
                id!!,
                surname,
                prename,
                Gender(gender),
                Birthday(birthday),
                absentManager.isAbsent(this),
                address,
                Town(town.id!!, town.zip, town.name),
                clazz.get(),
                Optional.ofNullable(sport)
        )
    }
}
