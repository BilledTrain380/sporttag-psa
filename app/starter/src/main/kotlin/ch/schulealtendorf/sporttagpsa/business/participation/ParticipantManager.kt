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

import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import java.util.NoSuchElementException
import java.util.Optional

/**
 * A manager for the participants of PSA.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface ParticipantManager {

    /**
     * @return a list of all participants
     */
    fun getParticipants(): List<ParticipantDto>

    /**
     * @return a list of participants related to the given [groupName]
     */
    fun getParticipantsByGroup(groupName: String): List<ParticipantDto>

    /**
     * @param id the id of the participant
     *
     * @return an optional containing the participant or empty if the participant could not be found
     */
    fun getParticipant(id: Int): Optional<ParticipantDto>

    /**
     * Saves the given [participant].
     *
     * If the [ParticipantDto.id] < 1, it will be created.
     *
     * If the participant exists already, it will be updated.
     *
     * If the [ParticipantDto.town] does not exist, it will be created
     *
     * The property [ParticipantDto.sportType] will be ignored.
     * To update this use [ParticipationManager.participate] or [ParticipationManager#reParticipate].
     *
     * @param participant the participant to save
     *
     * @return the created participant
     * @throws NoSuchElementException if the group of the given [participant] does not exist
     */
    fun saveParticipant(participant: ParticipantDto): ParticipantDto

    /**
     * Deletes the participant with the given [id].
     *
     * @param id the participant id
     */
    fun deleteParticipantById(id: Int)
}
