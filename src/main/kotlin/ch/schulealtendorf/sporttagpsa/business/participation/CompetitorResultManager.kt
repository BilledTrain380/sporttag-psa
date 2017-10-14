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

import ch.schulealtendorf.sporttagpsa.business.rulebook.CategoryModel
import ch.schulealtendorf.sporttagpsa.business.rulebook.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.entity.StarterEntity
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import ch.schulealtendorf.sporttagpsa.repository.ResultRepository
import ch.schulealtendorf.sporttagpsa.repository.StarterRepository
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.util.*

/**
 * {@link CompetitorResultManager} manages the results of a competitor
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class CompetitorResultManager(
        private val starterRepository: StarterRepository,
        private val resultRepository: ResultRepository,
        private val ruleBook: CategoryRuleBook,
        disciplineRepository: DisciplineRepository
): ResultManager {
    
    private val disciplines = disciplineRepository.findAll()

    /**
     * Creates results for each discipline for the passed in {@code competitor}.
     * The results distance is determined by the {@link CategoryRuleBook}.
     *
     * @param competitor the competitor to create the results for
     */
    override fun createResults(competitor: CompetitorEntity) {
        
        val starter = starterRepository.save(StarterEntity(null, competitor))
        
        disciplines.forEach {
            val distance = starter distanceFor  it.name
            
            val result = ResultEntity(null, distance,1, 1, starter, it)
            resultRepository.save(result)
        }
    }

    /**
     * Runs the rulebook with the starters age and the given discipline.
     * 
     * @return the resulting distance
     */
    private infix fun StarterEntity.distanceFor(discipline: String) = ruleBook.run(CategoryModel(competitor.birthday.age(), discipline))

    /**
     * @return the difference in years between now and the date
     */
    private fun Date.age(): Int {
        val todayYears = DateTime().year
        val actualYears = DateTime(this).year
        
        return todayYears - actualYears
    }
}