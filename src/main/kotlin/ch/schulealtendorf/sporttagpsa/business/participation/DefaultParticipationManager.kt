/*
 * Copyright (c) 2017 by Nicolas Märchy
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

import ch.schulealtendorf.sporttagpsa.entity.AbsentCompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * {@link DefaultParticipationManager} manages a participation for participantModelList.
 * 
 * @author nmaerchy
 * @version 1.2.0
 */
@Component
class DefaultParticipationManager(
        private val competitorRepository: CompetitorRepository,
        private val sportRepository: SportRepository,
        private val absentCompetitorRepository: AbsentCompetitorRepository,
        private val sportPreprocessor: SportPreprocessor
): ParticipationManager {

    /**
     * Returns a list of participants which are in the given {@code clazz}.
     * If the given {@code clazz} does not exists, an empty list will be returned.
     *
     * @param clazz the clazz to get its participants
     *
     * @return the resulting list
     */
    override fun getParticipantListByClazz(clazz: ClazzObj): List<ParticipantObj> {

        TODO("Not implemented yet")
//        val absentCompetitorList = absentCompetitorRepository.findAll()
//
//        return competitorRepository.findByClazzId(clazz.id)
//                .map {
//                    ParticipantObj(
//                            it.id!!,
//                            it.surname,
//                            it.prename,
//                            Gender(it.gender),
//                            Birthday(it.birthday),
//                            it.address,
//                            absentCompetitorList.any { absent -> absent.competitor.id == it.id },
//                            Optional.ofNullable(it.sport())
//                    )
//                }
    }

    /**
     * @return the participant matching the given {@code id}
     * @throws IllegalArgumentException if the participant matching the given id does not exists
     */
    override fun getParticipant(id: Int): SingleParticipant {

        val competitorEntity: CompetitorEntity = competitorRepository.findById(id)
                .orElseThrow { IllegalArgumentException("Could not find competitor: id$id") }!!

        return SingleParticipant(
                competitorEntity.id!!,
                competitorEntity.surname,
                competitorEntity.prename,
                Gender(competitorEntity.gender),
                competitorEntity.address
        )
    }

    /**
     * Updates the given {@code participant}.
     *
     * @param participant participant data to update
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    override fun updateParticipant(participant: SingleParticipant) {

        val competitorEntity: CompetitorEntity = competitorRepository.findById(participant.id)
                .orElseThrow { IllegalArgumentException("Could not find participant: id=${participant.id}") }!!

        competitorEntity.apply {
            prename = participant.prename
            surname = participant.surname
            gender = participant.gender.value
            address = participant.address
        }

        competitorRepository.save(competitorEntity)
    }

    /**
     * Sets the given {@code sport} on the given {@code participant}.
     * Invokes the {@link SportPreprocessor} before the sport will be set.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws IllegalArgumentException if either the participant or the sport could not be found
     */
    override fun setSport(participant: SingleParticipant, sport: Sport) {

        sportPreprocessor.accept(participant, sport)

        val competitorEntity: CompetitorEntity = competitorRepository.findById(participant.id)
                .orElseThrow { IllegalArgumentException("Could not find participant: id=${participant.id}") }!!

//        val sportEntity: SportEntity = sportRepository.findById(sport.id)
//                .orElseThrow { IllegalArgumentException("Could not find sport: id=${sport.id}") }!!
//
//        competitorEntity.sport = sportEntity

        competitorRepository.save(competitorEntity)
    }

    /**
     * Marks the given {@code participant} as absent.
     *
     * @param participant the participant to set as absent
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    override fun markAsAbsent(participant: SingleParticipant) {

        val competitorEntity: CompetitorEntity = competitorRepository.findById(participant.id)
                .orElseThrow { IllegalArgumentException("Could not find participant: id=${participant.id}") }!!

        val absentCompetitorList = absentCompetitorRepository.findAll()

        if (!absentCompetitorList.any { it.competitor.id == participant.id }) {
            absentCompetitorRepository.save(AbsentCompetitorEntity(null, competitorEntity))
        }
    }

    /**
     * As a counter part of {@code markAsAbsent},
     * marks the given {@code participant} as present.
     *
     * @param participant the participant to set as present
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    override fun markAsPresent(participant: SingleParticipant) {

        val absentCompetitor = absentCompetitorRepository.findByCompetitorId(participant.id)

        absentCompetitor.ifPresent {
            absentCompetitorRepository.delete(absentCompetitor.get())
        }
    }

    private fun CompetitorEntity.sport(): Sport? {

        TODO("Not implemented yet")
//        if (sport == null) {
//            return null
//        }
//
//        return Sport(sport!!.id!!, sport!!.name)
    }
}