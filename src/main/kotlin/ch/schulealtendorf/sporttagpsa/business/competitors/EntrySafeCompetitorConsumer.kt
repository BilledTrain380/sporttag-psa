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

import ch.schulealtendorf.sporttagpsa.entity.ClazzEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.business.parsing.FlatCompetitor
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.springframework.stereotype.Component
import java.sql.Date
import javax.persistence.EntityNotFoundException

/**
 * An implementation that consumes a {@link FlatCompetitor}
 * and ensures, that the {@link FlatCompetitor} attributes which are references to another Entity are available.
 * 
 * @author nmaerchy
 * @version 0.0.2
 */
@Component
class EntrySafeCompetitorConsumer(
        private val competitorRepository: CompetitorRepository,
        private val townRepository: TownRepository,
        private val clazzRepository: ClazzRepository
): CompetitorConsumer {

    /**
     * Saves a {@link CompetitorEntity} based on the passed in argument.
     * The {@link FlatCompetitor#clazz} attribute has to be exist as a ClazzEntity already.

     * @param competitor the input argument
     * @throws EntityNotFoundException if the {@link FlatCompetitor#clazz} attribute does not exist as a ClazzEntity already
     */
    override fun accept(competitor: FlatCompetitor) {
        
        val clazzEntity: ClazzEntity = clazzRepository.findByName(competitor.clazz) ?:
                throw EntityNotFoundException("Competitor $competitor expecting an existing ClazzEntity: ClazzEntity not found")
        
        val townEntity: TownEntity = townRepository.findByZipAndName(competitor.zipCode, competitor.town) ?:
                TownEntity(null, competitor.zipCode, competitor.town)
        
        val competitorEntity: CompetitorEntity = CompetitorEntity(
                null, competitor.surname, competitor.prename, competitor.gender, Date(competitor.birthday.time), competitor.address, townEntity, clazzEntity)
        
        competitorRepository.save(competitorEntity)    
    }
}