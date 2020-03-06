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

package ch.schulealtendorf.sporttagpsa.business.athletics

import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.ResultDto
import java.util.NoSuchElementException
import java.util.Optional

/**
 * A manager to handle {@link Competitor} data.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
interface CompetitorManager {

    /**
     * @return a list of all competitors
     */
    fun getCompetitorList(): List<CompetitorDto>

    /**
     * Get a competitor as a {@link Optional} matching the given {@code id}.
     *
     * If no competitor can be found an empty Optional will be returned.
     *
     * @param id the ID of the competitor
     *
     * @return an Optional containing the resulting competitor
     */
    fun getCompetitor(id: Int): Optional<CompetitorDto>

    /**
     * Saves the results of the given {@code competitor}.
     *
     * If the result id equals 0, it will be created.
     *
     * @param competitor the competitor to save its results
     *
     * @throws NoSuchElementException if the competitor does not exist
     */
    fun saveCompetitorResults(competitor: CompetitorDto)

    /**
     * Merges the results of the given {@code competitor} with the given {@code results}
     * based by the result id.
     *
     * If a result does not exist in the competitor's result list, it will be added.
     *
     * @param competitor the competitor to merge the results in
     * @param results the results to merge into the competitors results
     *
     * @return the competitor containing the merged results
     */
    fun mergeResults(competitor: CompetitorDto, results: Iterable<ResultDto>): CompetitorDto
}
