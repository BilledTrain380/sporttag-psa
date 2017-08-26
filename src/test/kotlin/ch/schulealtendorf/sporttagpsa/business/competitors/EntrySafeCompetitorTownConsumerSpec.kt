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
import ch.schulealtendorf.sporttagpsa.repository.TownRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.*

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object EntrySafeCompetitorTownConsumerSpec: Spek({

    var mockTownRepo: TownRepository = Mockito.mock(TownRepository::class.java)
    var consumer: EntrySafeCompetitorTownConsumer = EntrySafeCompetitorTownConsumer(mockTownRepo)

    beforeEachTest {
        mockTownRepo = Mockito.mock(TownRepository::class.java)
        consumer = EntrySafeCompetitorTownConsumer(mockTownRepo)
    }
    
    describe("an EntrySafeCompetitorTownConsumer") {
        
        val flatCompetitor: FlatCompetitor = FlatCompetitor(
                "", "", true, Date(1), "", "4000", "Musterhausen", "", "") // <- just empty values for non used attributes
        
        on("consuming a FlatCompetitor") {

            `when` (mockTownRepo.findByZipAndName("4000", "Musterhausen")).thenReturn(null)

            consumer.accept(flatCompetitor)
            
            it("should save a TownEntity based on the FlatCompetitors attributes") {
                val expected: TownEntity = TownEntity(null, "4000", "Musterhausen")
                Mockito.verify(mockTownRepo, Mockito.times(1)).save(expected)
            }
        }
        
        on("consuming a FlatCompetitor that zip and town attribute already exists") {

            val foundEntity: TownEntity = TownEntity(1, "4000", "Musterhausen")

            `when` (mockTownRepo.findByZipAndName("4000", "Musterhausen")).thenReturn(foundEntity)

            consumer.accept(flatCompetitor)
            
            it("should check if the TownEntity exists already") {
                Mockito.verify(mockTownRepo, Mockito.times(1)).findByZipAndName("4000", "Musterhausen")
            }
            
            it("should not save any TownEntity") {
                Mockito.verifyNoMoreInteractions(mockTownRepo)
            }
        }
    }
})