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

package ch.schulealtendorf.sporttagpsa.business.participant

import ch.schulealtendorf.sporttagpsa.model.Clazz
import ch.schulealtendorf.sporttagpsa.model.Participant
import java.util.*

/**
 * A manager for the participants of PSA.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface ParticipantManager {

    /**
     * @return all participants
     */
    fun getAllParticipants(): List<Participant>

    /**
     * Get all participant related to the given {@code clazz}.
     *
     * @param clazz the class to filter the participants
     *
     * @return all participant related to the given {@code clazz}.
     */
    fun getAllParticipants(clazz: Clazz): List<Participant>

    /**
     * Returns an Optional containing the participant matching
     * the given {@code participantId} or an empty Optional if
     * no participant could be found.
     *
     * @param participantId the id of the participant
     *
     * @return the resulting participant
     */
    fun getParticipant(participantId: Int): Optional<Participant>

    /**
     * Saves the given {@code participant}.
     * If the participant does not exists already, a new one is created.
     *
     * Any given relation is cascaded.
     *
     * @param participant the participant to update
     */
    fun saveParticipant(participant: Participant)
}
