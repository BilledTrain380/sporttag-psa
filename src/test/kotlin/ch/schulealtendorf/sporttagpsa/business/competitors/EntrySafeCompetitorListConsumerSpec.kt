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

import ch.schulealtendorf.sporttagpsa.business.parsing.FlatCompetitor
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

/**
 * @author nmaerchy
 * @version 0.0.1
 */
object EntrySafeCompetitorListConsumerSpec: Spek({
    
    var mockCompetitorConsumer: EntrySafeCompetitorConsumer = Mockito.mock(EntrySafeCompetitorConsumer::class.java)
    var mockTownConsumer: EntrySafeCompetitorTownConsumer = Mockito.mock(EntrySafeCompetitorTownConsumer::class.java)
    var mockTeacherConsumer: EntrySafeCompetitorTeacherConsumer = Mockito.mock(EntrySafeCompetitorTeacherConsumer::class.java)
    var mockClazzConsumer: EntrySafeCompetitorClazzConsumer = Mockito.mock(EntrySafeCompetitorClazzConsumer::class.java)
    var consumer: EntrySafeCompetitorListConsumer = EntrySafeCompetitorListConsumer(mockCompetitorConsumer, mockTownConsumer, mockTeacherConsumer, mockClazzConsumer)
    
    beforeEachTest {
        mockCompetitorConsumer = Mockito.mock(EntrySafeCompetitorConsumer::class.java)
        mockTownConsumer = Mockito.mock(EntrySafeCompetitorTownConsumer::class.java)
        mockTeacherConsumer = Mockito.mock(EntrySafeCompetitorTeacherConsumer::class.java)
        mockClazzConsumer = Mockito.mock(EntrySafeCompetitorClazzConsumer::class.java)
        consumer = EntrySafeCompetitorListConsumer(mockCompetitorConsumer, mockTownConsumer, mockTeacherConsumer, mockClazzConsumer)
    }
    
    describe("an EntrySafeCompetitorListConsumer") {
        
        val competitor: FlatCompetitor = FlatCompetitor("", "", true, Date(1), "", "", "", "", "")
        
        // the attributes does not matter, so empty all
        val competitorList: List<FlatCompetitor> = Stream.of(
                competitor,
                competitor
        ).collect(Collectors.toList())
        
        // Todo: use some argument matcher
        on("consuming a list of FlatCompetitor objects") {
            
            consumer.accept(competitorList)
            
            it("should delegate each element to the EntrySafeCompetitorTeacherConsumer") {
                Mockito.verify(mockTeacherConsumer, Mockito.times(2)).accept(competitor)
            }
            
            it("should delegate each element to the EntrySafeCompetitorClazzConsumer") {
                Mockito.verify(mockClazzConsumer, Mockito.times(2)).accept(competitor)
            }
            
            it("should delegate each element to the EntrySafeCompetitorTownConsumer") {
                Mockito.verify(mockTownConsumer, Mockito.times(2)).accept(competitor)
            }
            
            it("should delegate each element to the EntrySafeCompetitorConsumer") {
                Mockito.verify(mockCompetitorConsumer, Mockito.times(2)).accept(competitor)
            }
        }
    }
})