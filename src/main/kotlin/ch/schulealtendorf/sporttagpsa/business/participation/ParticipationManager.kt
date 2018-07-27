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

import ch.schulealtendorf.sporttagpsa.model.ClazzObj
import ch.schulealtendorf.sporttagpsa.model.ParticipantObj
import ch.schulealtendorf.sporttagpsa.model.SingleParticipant
import ch.schulealtendorf.sporttagpsa.model.Sport

/**
 * Describes a manager for the participation.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
interface ParticipationManager {

    /**
     * Returns a list of participants which are in the given {@code clazz}.
     * If the given {@code clazz} does not exists, an empty list will be returned.
     *
     * @param clazz the clazz to get its participants
     *
     * @return the resulting list
     */
    fun getParticipantListByClazz(clazz: ClazzObj): List<ParticipantObj>

    /**
     * @return the participant matching the given {@code id}
     * @throws IllegalArgumentException if the participant matching the given id does not exists
     */
    fun getParticipant(id: Int): SingleParticipant

    /**
     * Updates the given {@code participant}.
     *
     * @param participant participant data to update
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    fun updateParticipant(participant: SingleParticipant)

    /**
     * Sets the given {@code sport} on the given {@code participant}.
     * Invokes the {@link SportPreprocessor} before the sport will be set.
     *
     * @param participant the participant to set the sport on
     * @param sport the sport to set on the participant
     *
     * @throws IllegalArgumentException if either the participant or the sport could not be found
     */
    fun setSport(participant: SingleParticipant, sport: Sport)

    /**
     * Marks the given {@code participant} as absent.
     *
     * @param participant the participant to set as absent
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    fun markAsAbsent(participant: SingleParticipant)

    /**
     * As a counter part of {@code markAsAbsent},
     * marks the given {@code participant} as present.
     *
     * @param participant the participant to set as present
     *
     * @throws IllegalArgumentException if the given {@code participant} could not be found
     */
    fun markAsPresent(participant: SingleParticipant)
}