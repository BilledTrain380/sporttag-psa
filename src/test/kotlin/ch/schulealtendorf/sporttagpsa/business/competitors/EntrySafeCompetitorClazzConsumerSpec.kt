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
import ch.schulealtendorf.sporttagpsa.entity.ClazzEntity
import ch.schulealtendorf.sporttagpsa.repository.ClazzRepository
import ch.schulealtendorf.sporttagpsa.repository.TeacherRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import javax.persistence.EntityNotFoundException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy
 * @version 0.0.1
 */
object EntrySafeCompetitorClazzConsumerSpec: Spek({
    
    var mockClazzRepo: ClazzRepository = Mockito.mock(ClazzRepository::class.java)
    var mockTeacherRepo: TeacherRepository = Mockito.mock(TeacherRepository::class.java)
    var consumer: EntrySafeCompetitorClazzConsumer = EntrySafeCompetitorClazzConsumer(mockClazzRepo, mockTeacherRepo)
    
    beforeEachTest {
        mockClazzRepo= Mockito.mock(ClazzRepository::class.java)
        mockTeacherRepo = Mockito.mock(TeacherRepository::class.java)
        consumer = EntrySafeCompetitorClazzConsumer(mockClazzRepo, mockTeacherRepo)
    }
    
    describe("an EntrySafeCompetitorClazzConsumer") {

        val flatCompetitor: FlatCompetitor = FlatCompetitor(
                "", "", true, Date(1), "", "", "", "1a", "Hans Müller") // <- just empty values for non used attributes

//        val clazzEntity: ClazzEntity = ClazzEntity("1a", "Hans Müller")
        
        on("consuming a FlatCompetitor") {

            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.empty())
//            `when` (mockTeacherRepo.findByName("Hans Müller")).thenReturn(teacherEntity)
            
            consumer.accept(flatCompetitor)
            
            it("should find the CoachEntity for the clazz") {
                Mockito.verify(mockTeacherRepo, Mockito.times(1)).findByName("Hans Müller")
            }
            
            it("should save a ClazzEntity based on the FlatCompetitors attributes") {
//                val expected: ClazzEntity = ClazzEntity(null, "1a", teacherEntity)
//                Mockito.verify(mockClazzRepo, Mockito.times(1)).save(expected)
            }
        }
        
        on("consuming a FlatCompetitor with an unexpected teacher attribute") {

            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.empty())
            `when` (mockTeacherRepo.findByName("Hans Müller")).thenReturn(null)
            
            it("should throw an EntityNotFoundException that the CoachEntity does not exist") {
                val exception = assertFailsWith(EntityNotFoundException::class) {
                    consumer.accept(flatCompetitor)
                }
                
                assertEquals("Clazz 1a expecting an existing CoachEntity: No CoachEntity found", exception.message)
            }
        }
        
        on("consuming a FlatCompetitor that clazz attribute already exists") {
            
//            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.of(clazzEntity))
            
            consumer.accept(flatCompetitor)
            
            it("should check if the ClazzEntity exists already") {
                verify(mockClazzRepo, times(1)).findByName("1a")
            }
            
            it("should not save any ClazzEntity") {
                verifyNoMoreInteractions(mockClazzRepo)
            }
            
        }
    }
})