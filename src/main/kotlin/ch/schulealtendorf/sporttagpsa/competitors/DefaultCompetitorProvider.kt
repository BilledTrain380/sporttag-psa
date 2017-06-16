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

package ch.schulealtendorf.sporttagpsa.competitors

import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.SportEntity
import ch.schulealtendorf.sporttagpsa.entity.map
import ch.schulealtendorf.sporttagpsa.entity.merge
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.springframework.stereotype.Component

/**
 * @author nmaerchy
 * @version 0.0.2
 */
@Component
class DefaultCompetitorProvider(
        private val competitorRepository: CompetitorRepository,
        private val sportRepository: SportRepository
): CompetitorProvider {

    override fun getCompetitorsByClazz(clazzId: Int): List<SimpleCompetitorModel> {
        return competitorRepository.findByClazzId(clazzId).map { it.map() }
    }

    override fun getCompetitorById(competitorId: Int): SimpleCompetitorModel {
        // just throw the NPE here, so it would redirect the user to the error page
        return competitorRepository.findOne(competitorId)!!.map()
    }

    override fun updateCompetitor(competitor: SimpleCompetitorModel) {
        
        // just throw the NPE here, so it would redirect the user to the error page
        val competitorEntity: CompetitorEntity = competitorRepository.findOne(competitor.id)!!
        val sportEntity: SportEntity? = sportRepository.findOne(competitor.sport.id)
        
        competitorEntity.merge(competitor)
        competitorEntity.sport = sportEntity
        
        competitorRepository.save(competitorEntity)
    }
}