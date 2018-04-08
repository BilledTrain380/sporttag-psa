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

import ch.schulealtendorf.sporttagpsa.model.SingleParticipant
import ch.schulealtendorf.sporttagpsa.model.Sport
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.springframework.stereotype.Component

/**
 * Sport middleware that only performs, when the {@link ParticipationStatus.isFinished} returns true.
 *
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class DefaultSportMiddleware(
        private val participationStatus: ParticipationStatus,
        private val starterManager: StarterManager,
        private val competitorRepository: CompetitorRepository
): SportMiddleware {

    /**
     * Creates an according {@link StarterEntity} for te given {@code participant},
     * if the given {@code Sport} equals "Mehrkampf".
     *
     * Otherwise removes the according {@link StarterEntity}.
     *
     * If {@link ParticipationStatus.isFinished} returns false, this method will be skipped.
     *
     * @param participant the participant that is set the sport
     * @param sport the sport that is set on the participant
     */
    override fun accept(participant: SingleParticipant, sport: Sport) {

        if (!participationStatus.isFinished()) {
            return
        }

        try {

            if (sport.name == "Mehrkampf") {
                starterManager.createStarter(
                        competitorRepository.findOne(participant.id)!!
                )
            } else {
                starterManager.removeStarter(
                        competitorRepository.findOne(participant.id)!!
                )
            }

        } catch (ex: StarterAlreadyExistsException) {
            // TODO: Add logger
        }
    }
}