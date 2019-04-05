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

import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.DisciplineDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ParticipantDto
import ch.schulealtendorf.psa.dto.ParticipationStatusDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.psa.dto.SportDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.psa.dto.UserDto

// Data classes representing the JSON object, where the model class itself does not fit.

data class RestParticipant @JvmOverloads constructor(
        val id: Int,
        val surname: String,
        val prename: String,
        val gender: GenderDto,
        val birthday: Long,
        val absent: Boolean,
        val address: String,
        val town: TownDto,
        val group: GroupDto,
        val sport: SportDto? = null
)

data class RestParticipationStatus(
        val status: ParticipationStatusDto
)

data class RestCompetitor(
        val id: Int,
        val startNumber: Int,
        val surname: String,
        val prename: String,
        val gender: GenderDto,
        val birthday: Long,
        val absent: Boolean,
        val address: String,
        val town: TownDto,
        val group: GroupDto,
        val results: List<RestResult>
)

data class RestResult(
        val id: Int,
        val value: Long,
        val points: Int,
        val distance: String?,
        val discipline: DisciplineDto
)

data class RestUser(
        val id: Int,
        val username: String,
        val enabled: Boolean
)

// Factory functions to create a data class representing the the JSON object of the given parameter.

fun json(participant: ParticipantDto): RestParticipant {
    return RestParticipant(
            participant.id,
            participant.surname,
            participant.prename,
            participant.gender,
            participant.birthday.milliseconds,
            participant.absent,
            participant.address,
            participant.town,
            participant.group,
            participant.sport.orElseGet { null }
    )
}

fun json(participationStatus: ParticipationStatusDto): RestParticipationStatus {
    return RestParticipationStatus(participationStatus)
}

fun json(competitor: CompetitorDto): RestCompetitor {
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
            competitor.group,
            competitor.results.map { json(it) }
    )
}

fun json(result: ResultDto): RestResult {
    return RestResult(
            result.id,
            result.value,
            result.points,
            result.distance.orElseGet { null },
            result.discipline
    )
}

fun json(user: UserDto): RestUser {
    return RestUser(
            user.id,
            user.username,
            user.enabled
    )
}
