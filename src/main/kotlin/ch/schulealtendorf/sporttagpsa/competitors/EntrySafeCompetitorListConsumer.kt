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

package ch.schulealtendorf.sporttagpsa.competitors

import ch.schulealtendorf.sporttagpsa.parsing.FlatCompetitor

/**
 * @author nmaerchy
 * @version 0.0.1
 */
class EntrySafeCompetitorListConsumer(
        private val competitorConsumer: CompetitorConsumer,
        private val competitorTownConsumer: CompetitorTownConsumer,
        private val competitorTeacherConsumer: CompetitorTeacherConsumer,
        private val competitorClazzConsumer: CompetitorClazzConsumer
): CompetitorListConsumer {

    /**
     * Performs this operation on the given argument.

     * @param t the input argument
     */
    override fun accept(t: List<FlatCompetitor>) {
        
        t.forEach { competitor: FlatCompetitor -> competitorTeacherConsumer.accept(competitor) }
        t.forEach { competitor: FlatCompetitor -> competitorClazzConsumer.accept(competitor) }
        t.forEach { competitor: FlatCompetitor -> competitorTownConsumer.accept(competitor) }
        t.forEach { competitor: FlatCompetitor -> competitorConsumer.accept(competitor) }
    }
}