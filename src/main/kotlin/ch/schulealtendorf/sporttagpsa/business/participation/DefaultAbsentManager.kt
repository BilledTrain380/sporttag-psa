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

import ch.schulealtendorf.sporttagpsa.entity.AbsentCompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
import org.springframework.stereotype.Component

/**
 * Default implementation for absent manager which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class DefaultAbsentManager(
        private val absentCompetitorRepository: AbsentCompetitorRepository
): AbsentManager {

    /**
     * @return true if the given {@code competitor} is absent, otherwise false
     */
    override fun isAbsent(competitor: CompetitorEntity): Boolean {

        if (competitor.id == null) return false

        val absentCompetitor = absentCompetitorRepository.findByCompetitorId(competitor.id!!)

        return absentCompetitor.isPresent
    }

    /**
     * Marks the given {@code competitor} as absent.
     *
     * If the competitor is already marked as absent, this method will do nothing.
     *
     * @param competitor the competitor to mark as absent
     */
    override fun markAsAbsent(competitor: CompetitorEntity) {

        val absentCompetitorEntity = absentCompetitorRepository.findByCompetitorId(competitor.id!!)

        if (absentCompetitorEntity.isPresent) {
            return
        }

        val absentCompetitor = AbsentCompetitorEntity(null, competitor)

        absentCompetitorRepository.save(absentCompetitor)
    }

    /**
     * Marks the given {@code competitor} as present.
     *
     * If the competitor is already marked as present, this method will do nothing.
     *
     * @param competitor the competitor to mark as present
     *
     * @throws NoSuchElementException if the given {@competitor} could not be found
     */
    override fun markAsPresent(competitor: CompetitorEntity) {

        val absentCompetitorEntity = absentCompetitorRepository.findByCompetitorId(competitor.id!!)

        absentCompetitorEntity.ifPresent {
            absentCompetitorRepository.delete(absentCompetitorEntity.get())
        }
    }
}
