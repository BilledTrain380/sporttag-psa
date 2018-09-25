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

package ch.schulealtendorf.sporttagpsa.controller.rest

import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.model.*
import org.springframework.stereotype.Component

/**
 * An implementation for {@link Mapper} to map rest models.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class RestMapper(
        private val groupManager: GroupManager
): Mapper {

    override fun of(participant: Participant): RestParticipant {
        return RestParticipant(
                participant.id,
                participant.surname,
                participant.prename,
                participant.gender,
                participant.birthday.milliseconds,
                participant.absent,
                participant.address,
                participant.town,
                of(participant.group),
                participant.sport.orElseGet { null }
        )
    }

    override fun of(group: Group): RestGroup {
        return RestGroup(
                group.name,
                group.coach.name,
                groupManager.hasPendingParticipation(group)
        )
    }

    override fun of(participationStatus: ParticipationStatus): RestParticipationStatus {
        return RestParticipationStatus(participationStatus)
    }

    override fun of(competitor: Competitor): RestCompetitor {
        return RestCompetitor(
                competitor.id,
                competitor.startNumber,
                competitor.surname,
                competitor.prename,
                competitor.gender,
                competitor.birthday.milliseconds,
                competitor.absent,
                competitor.address,
                competitor.town,
                of(competitor.group),
                competitor.results.map { of(it) }
        )
    }

    override fun of(result: Result): RestResult {
        return RestResult(
                result.id,
                result.value,
                result.points,
                result.distance.orElseGet { null },
                result.discipline
        )
    }
}