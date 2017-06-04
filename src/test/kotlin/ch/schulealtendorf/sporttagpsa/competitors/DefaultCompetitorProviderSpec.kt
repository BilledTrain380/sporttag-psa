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

import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.model.SportModel
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.stream.Collectors
import java.util.stream.Stream
import kotlin.test.assertEquals

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object DefaultCompetitorProviderSpec: Spek({
    
    var mockCompetitorRepo: CompetitorRepository = Mockito.mock(CompetitorRepository::class.java)
    var mockSportRepo: SportRepository = Mockito.mock(SportRepository::class.java)
    var provider: DefaultCompetitorProvider = DefaultCompetitorProvider(mockCompetitorRepo, mockSportRepo)
    
    beforeEachTest {
        mockCompetitorRepo = Mockito.mock(CompetitorRepository::class.java)
        mockSportRepo =  Mockito.mock(SportRepository::class.java)
        provider = DefaultCompetitorProvider(mockCompetitorRepo, mockSportRepo)
    }
    
    describe("a DefaultCompetitorProvider") {
        
        
        on("getting a list of competitors by clazz") {
            
            val townEntity: TownEntity = TownEntity(1, "8000", "Musterhausen")
            val clazzEntity: ClazzEntity = ClazzEntity(1, "1a", TeacherEntity(1, "teacher"))
            val sportEntity: SportEntity = SportEntity(1, "Brennball")
            
            `when` (mockCompetitorRepo.findByClazzId(1)).thenReturn(Stream.of(
                    CompetitorEntity(1, "Muster", "Hans", true, java.sql.Date(1), "address",
                            townEntity,
                            clazzEntity,
                            null),
                    
                    CompetitorEntity(2, "Wirbelwind", "Will", false, java.sql.Date(1), "address",
                            townEntity,
                            clazzEntity,
                            sportEntity)
                    
            ).collect(Collectors.toList()))
            
            it("should map the CompetitorEntity clazz to a SimpleCompetitorModel") {
                
                val expected: List<SimpleCompetitorModel> = Stream.of(
                        SimpleCompetitorModel(1, "Muster", "Hans", true, SportModel()),
                        SimpleCompetitorModel(2, "Wirbelwind", "Will", false, SportModel(1, "Brennball"))
                ).collect(Collectors.toList())
                
                assertEquals(expected, provider.getCompetitorsByClazz(1))
            }
        }
        
        
    }
    
})