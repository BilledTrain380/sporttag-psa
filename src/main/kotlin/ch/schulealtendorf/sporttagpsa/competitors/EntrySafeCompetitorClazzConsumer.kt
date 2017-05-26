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

import ch.schulealtendorf.sporttagpsa.entity.ClazzEntity
import ch.schulealtendorf.sporttagpsa.entity.TeacherEntity
import ch.schulealtendorf.sporttagpsa.parsing.FlatCompetitor
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.TeacherRepository
import org.springframework.stereotype.Component
import javax.persistence.EntityNotFoundException

/**
 * An implementation that consumes a {@link FlatCompetitor}
 * and ensures, that the {@link FlatCompetitor} attributes for a ClazzEntity are only consumed once.
 * 
 * @author nmaerchy
 * @version 0.0.2
 */
@Component
class EntrySafeCompetitorClazzConsumer(
        private val clazzRepository: ClazzRepository,
        private val teacherRepository: TeacherRepository
): CompetitorClazzConsumer {

    /**
     * Saves a {@link ClazzEntity} based on the passed in argument.
     * The {@link FlatCompetitor#teacher} attribute has to be exist as a TeacherEntity already.

     * @param competitorList the input argument
     * @throws EntityNotFoundException if the {@link FlatCompetitor#teacher} attribute does not exist as a TeacherEntity already
     */
    override fun accept(competitorList: FlatCompetitor) {
        
        if (clazzRepository.findByName(competitorList.clazz) == null) {
            val teacherEntity: TeacherEntity = teacherRepository.findByName(competitorList.teacher) ?:
                    throw EntityNotFoundException("Clazz ${competitorList.clazz} expecting an existing TeacherEntity: No TeacherEntity found")

            val clazzEntity: ClazzEntity = ClazzEntity(competitorList.clazz, teacherEntity)

            clazzRepository.save(clazzEntity)
        }
    }
}