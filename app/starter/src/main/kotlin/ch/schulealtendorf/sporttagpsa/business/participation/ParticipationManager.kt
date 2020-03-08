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

import ch.schulealtendorf.psa.dto.participation.ParticipantDto
import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType
import java.util.NoSuchElementException

/**
 * Describes a manager for the participation.
 * Provides various operations with a participant. In addition,
 * the participation status can be modified.
 *
 * @author nmaerchy
 * @since 1.0.0
 */
interface ParticipationManager {

    /**
     * Sets the given [sport] to the given [participant].
     *
     * This operation can not be performed, if the [ParticipationManager.getParticipationStatus] equals [ParticipationStatusType.CLOSED].
     * In order to change the sport of a participant, use the [ParticipationManager.reParticipate] method.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws NoSuchElementException if the given participant could not be found
     * @throws IllegalStateException if the participation is already closed
     */
    fun participate(participant: ParticipantDto, sport: String)

    /**
     * Sets the given [sport] to the given [participant].
     *
     * In contrast to the [ParticipationManager.participate] method, this operation will
     * consider the participation status.
     *
     * If the [ParticipationManager.getParticipationStatus] equals [ParticipationStatusType.CLOSED],
     * and the given [sport] equals athletics, the participant will be saved as a competitor.
     *
     * If the [ParticipationManager.getParticipationStatus] [equals ParticipationStatusType.CLOSED],
     * and the given [participant] is already a competitor, but the given [sport] is not athletics,
     * the competitor will be removed.
     *
     * The [ParticipationManager.getParticipationStatus] must be equal to [ParticipationStatusType.CLOSED]
     * in order to perform this operation. Otherwise use the [ParticipationManager.participate] method.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws NoSuchElementException if the given participant could not be found
     * @throws IllegalStateException if the participation status is not CLOSE.
     */
    fun reParticipate(participant: ParticipantDto, sport: String)

    /**
     * Closes the participation. [ParticipationManager.getParticipationStatus] will always
     * return [ParticipationStatusType.CLOSED] until [ParticipationManager.resetParticipation]
     * will be invoked.
     *
     * This operation looks up all participant who participates in the sport athletics
     * and saves them as competitor with default results of all available disciplines.
     */
    fun closeParticipation()

    /**
     * Resets the participation. All recorded data will be DELETED.
     *
     * [ParticipationManager.getParticipationStatus] will always return [ParticipationStatusType.OPEN]
     * until [ParticipationManager.closeParticipation] will be invoked.
     */
    fun resetParticipation()

    /**
     * @return the participation status
     */
    fun getParticipationStatus(): ParticipationStatusType
}
