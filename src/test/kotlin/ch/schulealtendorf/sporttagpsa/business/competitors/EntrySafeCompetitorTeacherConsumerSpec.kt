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
import ch.schulealtendorf.sporttagpsa.repository.CoachRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*

/**
 * @author nmaerchy
 * @version 0.0.1
 */
object EntrySafeCompetitorTeacherConsumerSpec: Spek({
    
    var mockTeacherRepo: CoachRepository = Mockito.mock(CoachRepository::class.java)
    var consumer: EntrySafeCompetitorTeacherConsumer = EntrySafeCompetitorTeacherConsumer(mockTeacherRepo)
    
    beforeEachTest {
        mockTeacherRepo = Mockito.mock(CoachRepository::class.java)
        consumer = EntrySafeCompetitorTeacherConsumer(mockTeacherRepo)
    }
    
    describe("a EntrySafeCompetitorTeacherConsumer") {

        val flatCompetitor: FlatCompetitor = FlatCompetitor(
                "", "", true, Date(1), "", "", "", "", "Hans Müller") // <- just empty values for non used attributes
        
        on("consuming a FlatCompetitor") {

            `when` (mockTeacherRepo.findByName("Hans Müller")).thenReturn(null)
            
            consumer.accept(flatCompetitor)
            
            it("should save a CoachEntity based on the FlatCompetitors attributes") {
//                val expected = CoachEntity(null, "Hans Müller")
//                Mockito.verify(mockTeacherRepo, Mockito.times(1)).save(expected)
            }
        }
        
        on("consuming a FlatCompetitor that teacher attributes already exists") {
            
//            val foundEntity: CoachEntity = CoachEntity(1, "Hans Müller")
            
//            `when` (mockTeacherRepo.findByName("Hans Müller")).thenReturn(foundEntity)
            
            consumer.accept(flatCompetitor)
            
            it("should check if the CoachEntity already exists") {
                verify(mockTeacherRepo, times(1)).findByName("Hans Müller")
            }
            
            it("should not save any CoachEntity") {
                verifyNoMoreInteractions(mockTeacherRepo)
            }
        }
    }
})