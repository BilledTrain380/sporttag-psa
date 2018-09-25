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

import ch.schulealtendorf.sporttagpsa.model.*

data class RestGroup(
        val name: String,
        val coach: String,
        val pendingParticipation: Boolean
)

data class RestParticipant @JvmOverloads constructor(
        val id: Int,
        val surname: String,
        val prename: String,
        val gender: Gender,
        val birthday: Long,
        val absent: Boolean,
        val address: String,
        val town: Town,
        val group: RestGroup,
        val sport: Sport? = null
)

@Deprecated("Use Sport model instead")
data class RestSport(
        val name: String
)

data class RestParticipationStatus(
        val status: ParticipationStatus
)

data class RestCompetitor(
        val id: Int,
        val startNumber: Int,
        val surname: String,
        val prename: String,
        val gender: Gender,
        val birthday: Long,
        val absent: Boolean,
        val address: String,
        val town: Town,
        val group: RestGroup,
        val results: List<RestResult>
)

data class RestResult(
        val id: Int,
        val value: Long,
        val points: Int,
        val distance: String?,
        val discipline: Discipline
)