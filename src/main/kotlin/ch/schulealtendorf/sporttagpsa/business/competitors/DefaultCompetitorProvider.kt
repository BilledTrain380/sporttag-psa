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
import ch.schulealtendorf.sporttagpsa.controller.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.entity.map
import ch.schulealtendorf.sporttagpsa.entity.merge
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component

/**
 * Provider for competitors.
 * 
 * @author nmaerchy
 * @version 0.0.2
 */
@Component
class DefaultCompetitorProvider(
        private val competitorRepository: CompetitorRepository,
        private val sportRepository: SportRepository,
        private val participationStatus: ParticipationStatus
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
            val sportEntity: SportEntity? = sportRepository.findOne(competitor.sport.id)

            competitorEntity.merge(competitor)
            competitorEntity.sport = sportEntity

            competitorRepository.save(competitorEntity)
        }
    }
}