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
import ch.schulealtendorf.sporttagpsa.entity.TownEntity
import ch.schulealtendorf.sporttagpsa.repository.GroupRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
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
object EntrySafeCompetitorConsumerSpec: Spek({
    
    var mockTownRepo: TownRepository = Mockito.mock(TownRepository::class.java)
    var mockClazzRepo: GroupRepository = Mockito.mock(GroupRepository::class.java)
    var mockCompetitorRepo: ParticipantRepository = Mockito.mock(ParticipantRepository::class.java)
    var consumer: EntrySafeCompetitorConsumer = EntrySafeCompetitorConsumer(mockCompetitorRepo, mockTownRepo, mockClazzRepo)
    
    beforeEachTest {
        mockTownRepo = Mockito.mock(TownRepository::class.java)
        mockClazzRepo = Mockito.mock(GroupRepository::class.java)
        mockCompetitorRepo = Mockito.mock(ParticipantRepository::class.java)
        consumer = EntrySafeCompetitorConsumer(mockCompetitorRepo, mockTownRepo, mockClazzRepo)
    }
    
    describe("an EntrySafeCompetitorConsumer") {

        val flatCompetitor: FlatCompetitor = FlatCompetitor(
                "Muster", "Hans", true, java.util.Date(1), "Musterstrasse 6", "4000", "Musterhausen", "1a", "Marry Müller")
        
        val townEntity: TownEntity = TownEntity(1, "4000", "Musterhausen")
//        val teacherEntity: CoachEntity = CoachEntity(1, "Marry Müller")
//        val clazzEntity: ClazzEntity = ClazzEntity(1, "1a", teacherEntity)
        
        on("consuming a FlatCompetitor") {
            
//            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.of(clazzEntity))
            `when` (mockTownRepo.findByZipAndName("4000", "Musterhausen")).thenReturn(Optional.of(townEntity))
            
            consumer.accept(flatCompetitor)
            
            it("should find the ClazzEntity for the clazz") {
                Mockito.verify(mockClazzRepo, Mockito.times(1)).findByName("1a")
            }
            
            it("should find the TownEntity for the town") {
                Mockito.verify(mockTownRepo, Mockito.times(1)).findByZipAndName("4000", "Musterhausen")
            }
            
            it("should save a CompetitorEntity based on the FlatCompetitors attributes") {
//                val expected: CompetitorEntity = CompetitorEntity(null, "Muster", "Hans", true, 1, "Musterstrasse 6", townEntity, clazzEntity)
//                Mockito.verify(mockCompetitorRepo, Mockito.times(1)).save(expected)
            }
        }
        
        on("consuming a FlatCompetitor with an unexpected clazz attribute") {
            
            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.empty())
            
            it("should throw an EntityNotFoundException that the clazz entity does not exist") {
                val expected: EntityNotFoundException = assertFailsWith(EntityNotFoundException::class) {
                    consumer.accept(flatCompetitor)
                }
                
                assertEquals("Competitor $flatCompetitor expecting an existing ClazzEntity: ClazzEntity not found", expected.message)
            }
        }

        on("consuming a FlatCompetitor with an non existing TownEntity") {

//            `when` (mockClazzRepo.findByName("1a")).thenReturn(Optional.of(clazzEntity))
            `when` (mockTownRepo.findByZipAndName("4000", "Musterhausen")).thenReturn(Optional.empty())

            consumer.accept(flatCompetitor)
            
            it("should save the TownEntity with the CompetitorEntity") {
                val newTownEntity: TownEntity = TownEntity(null, "4000", "Musterhausen") // it is important, that the id is not set, otherwise it would be an existing TownEntity
//                val expected: CompetitorEntity = CompetitorEntity(null, "Muster", "Hans", true, 1, "Musterstrasse 6", newTownEntity, clazzEntity)
//                verify(mockCompetitorRepo, times(1)).save(expected)
            }
        }
    }
})