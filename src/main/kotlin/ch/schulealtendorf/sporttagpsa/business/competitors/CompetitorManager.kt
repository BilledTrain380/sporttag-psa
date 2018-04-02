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

package ch.schulealtendorf.sporttagpsa.business.competitors

import ch.schulealtendorf.sporttagpsa.model.Competitor
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitor

/**
 * Describes a provider for competitors.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
interface CompetitorManager {

    /**
     * Gets all competitors that belong to the clazz with the passed in argument.
     *
     * @param clazzId the id of the clazz wanted
     * @return a list of found competitors
     */
    fun getCompetitorsByClazz(clazzId: Int): List<SimpleCompetitorModel>


    /**
     * Get all competitors that belongs the the class matching the given {@code clazzId}.
     *
     * If the {@code clazzId} does not exists, an empty list will be returned.
     *
     * @param clazzId the id of the class
     *
     * @return a list of competitors
     */
    fun getCompetitorListByClazz(clazzId: Int): List<Competitor>

    /**
     * Returns a {@link SimpleCompetitor} matching the given {@code id}.
     *
     * @param id id of the competitor
     *
     * @return the resulting competitor
     * @throws CompetitorNotFoundException if the given {@code id} does no exists
     */
    fun getCompetitor(id: Int): SimpleCompetitor

    /**
     * Saves the given {@code competitor}.
     *
     * @param competitor competitor data to save
     */
    fun saveCompetitor(competitor: SimpleCompetitor)

    /**
     * Sets the sport matching the given {@code sportId}
     * to the competitor matching the given {@code competitorId}.
     *
     * This method considers the {@link ParticipationStatus}.
     *
     * @param competitorId the competitor id to set the sport for
     * @param sportId the sport id to set to the competitor
     *
     * @throws IllegalStateException if the participation is already finished
     */
    fun setSport(competitorId: Int, sportId: Int)

    /**
     * Un-sets the sport of the competitor matching the given {@code competitorId}.
     *
     * This method considers the {@link ParticipationStatus}.
     *
     * @param competitorId the competitor id to set the sport for
     * @throws IllegalStateException if the participation is already finished
     */
    fun unsetSport(competitorId: Int)

    /**
     * Marks the competitor matching the given {@code competitorId} as absent.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as absent
     */
    fun markAsAbsent(competitorId: Int)

    /**
     * Counter part of {@code markAsAbsent}.
     * Marks the competitor matching the given {@code competitorId} as present.
     * If the given {@code competitorId} does not exists, this method will do nothing.
     *
     * @param competitorId id of the competitor to mark as present
     */
    fun markAsPresent(competitorId: Int)
}