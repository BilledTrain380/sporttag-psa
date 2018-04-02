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

package ch.schulealtendorf.sporttagpsa.business.competitors

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.Birthday
import ch.schulealtendorf.sporttagpsa.model.Competitor
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.model.Sport
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component
import java.util.*

/**
 * Provider for competitors.
 * 
 * @author nmaerchy
 * @version 1.1.0
 */
@Component
class DefaultCompetitorProvider(
        private val competitorRepository: CompetitorRepository,
        private val sportRepository: SportRepository,
        private val participationStatus: ParticipationStatus,
        private val absentCompetitorRepository: AbsentCompetitorRepository
): CompetitorProvider {

    /**
     * Gets all competitors that belong to the clazz with the passed in argument.
     * 
     * @param clazzId the id of the clazz wanted
     * @return a list of found competitors
     */
    override fun getCompetitorsByClazz(clazzId: Int): List<SimpleCompetitorModel> {
        return competitorRepository.findByClazzId(clazzId).map { it.map() }
    }

    /**
     * Gets a single competitor by the passed in argument.
     * 
     * @param competitorId id of the competitor wanted
     * @return the found competitor
     * @throws NullPointerException If no competitor belongs to the passed in argument.
     */
    override fun getCompetitorById(competitorId: Int) = competitorRepository.findOne(competitorId)!!.map()

    /**
     * Update all properties of a single competitor depending on the passed in argument.
     * 
     * @param competitor competitor model containing properties to update
     * @throws NullPointerException If the id of the passed in competitor does not exist.
     */
    override fun updateCompetitor(competitor: SimpleCompetitorModel) {
        
        if (!participationStatus.isFinished()) {

            val competitorEntity: CompetitorEntity = competitorRepository.findOne(competitor.id)!!
            val sportEntity: SportEntity? = if(competitor.sport == null) null else sportRepository.findOne(competitor.sport.id)

            competitorEntity.merge(competitor)
            competitorEntity.sport = sportEntity

            competitorRepository.save(competitorEntity)
        }
    }

    /**
     * Get all competitors that belongs the the class matching the given {@code clazzId}.
     *
     * If the {@code clazzId} does not exists, an empty list will be returned.
     *
     * @param clazzId the id of the class
     *
     * @return a list of competitors
     */
    override fun getCompetitorListByClazz(clazzId: Int): List<Competitor> {

        val absentCompetitorList = absentCompetitorRepository.findAll()

        return competitorRepository.findByClazzId(clazzId)
                .map {
                    Competitor(
                            it.id!!,
                            it.surname,
                            it.prename,
                            Gender(it.gender),
                            Birthday(it.birthday),
                            it.address,
                            absentCompetitorList.any { absent -> absent.competitor.id == it.id },
                            Optional.ofNullable(it.sport())
                    )
                }
    }

    /**
     * Marks the competitor matching the given {@code competitorId} as absent.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as absent
     */
    override fun markAsAbsent(competitorId: Int) {

        val competitorEntity = competitorRepository.findOne(competitorId)

        if (competitorEntity != null) {

            val absentCompetitorList = absentCompetitorRepository.findAll()

            if (!absentCompetitorList.any { it.competitor.id == competitorId }) {
                absentCompetitorRepository.save(AbsentCompetitorEntity(null, competitorEntity))
            }
        }
    }

    /**
     * Counter part of {@code markAsAbsent}.
     * Marks the competitor matching the given {@code competitorId} as present.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as present
     */
    override fun markAsPresent(competitorId: Int) {

        val absentCompetitor = absentCompetitorRepository.findByCompetitorId(competitorId)

        if (absentCompetitor != null) {
            absentCompetitorRepository.delete(absentCompetitor)
        }
    }

    private fun CompetitorEntity.sport(): Sport? {

        if (sport == null) {
            return null
        }

        return Sport(sport!!.id!!, sport!!.name)
    }
}