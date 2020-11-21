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

package ch.schulealtendorf.psa.service.participation.business

import ch.schulealtendorf.psa.dto.participation.ParticipationStatusType

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
