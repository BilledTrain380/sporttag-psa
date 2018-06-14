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

package ch.schulealtendorf.sporttagpsa.business.tournament

import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.ResultRepository
import org.springframework.stereotype.Component

/**
 * {@link PersistenceTournamentProvider} provides tournament data
 * from a persistence source.
 * 
 * @author nmaerchy
 * @version 2.0.0
 */
@Component
class PersistenceTournamentProvider(
        private val resultRepository: ResultRepository,
        private val absentCompetitorRepository: AbsentCompetitorRepository
): TournamentProvider {

    /**
     * Finds all competitors by the given filter.
     *
     * @param filter the filter to get competitors
     *
     * @return the resulting competitor list
     */
    override fun findByFilter(filter: TournamentFilter): List<TournamentCompetitor> {

        val absentCompetitorList = absentCompetitorRepository.findAll()

        val results = resultRepository.findByDisciplineIdAndStarterCompetitorGenderAndStarterCompetitorClazzId(filter.disciplineId, filter.gender, filter.clazzId)

        return results
                .filter { !absentCompetitorList.any { absent -> absent.competitor.id == it.starter.competitor.id } }
                .map {
                    TournamentCompetitor(
                            it.starter.number!!,
                            it.id!!,
                            it.starter.competitor.prename,
                            it.starter.competitor.surname,
                            it.starter.competitor.gender,
                            it.distance,
                            it.result,
                            it.discipline.unit.unit,
                            it.points
                    )
                }
    }

    /**
     * Updates the result of the given competitor.
     *
     * @param result holds the result to update
     */
    override fun updateResult(result: TournamentResult) {
        
        val resultEntity = resultRepository.findById(result.id).get()
        resultEntity.result = result.result
        resultEntity.points = result.points
        
        resultRepository.save(resultEntity)
    }
}