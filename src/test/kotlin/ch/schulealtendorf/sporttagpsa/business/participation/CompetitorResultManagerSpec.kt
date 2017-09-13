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

package ch.schulealtendorf.sporttagpsa.business.participation

import ch.schulealtendorf.sporttagpsa.business.rulebook.CategoryModel
import ch.schulealtendorf.sporttagpsa.business.rulebook.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import ch.schulealtendorf.sporttagpsa.repository.ResultRepository
import ch.schulealtendorf.sporttagpsa.repository.StarterRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.joda.time.DateTime
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import java.sql.Date

/**
 * Spec for {@link CompetitorResultManager}.
 * 
 * @author nmaerchy
 * *
 * @version 1.0.0
 */
@RunWith(JUnitPlatform::class)
object CompetitorResultManagerSpec : Spek({
    
    describe("a result manager") {
        
        val testDisciplines: Iterable<DisciplineEntity> = arrayListOf(
                DisciplineEntity(1,"Weitsprung", UnitEntity()),
                DisciplineEntity(2,"Weitwurf", UnitEntity())
        )
        
        val mockStarterRepository: StarterRepository = mock()
        val mockResultRepository: ResultRepository = mock()
        val mockDisciplineRepository: DisciplineRepository = mock()
        val mockRuleBook: CategoryRuleBook = mock()
        
        var manager = CompetitorResultManager(mockStarterRepository, mockResultRepository, mockRuleBook, mockDisciplineRepository)
        
        beforeEachTest { 
            reset(mockStarterRepository, mockResultRepository, mockDisciplineRepository, mockRuleBook)
            whenever(mockDisciplineRepository.findAll())
                    .thenReturn(testDisciplines)
            
            manager = CompetitorResultManager(mockStarterRepository, mockResultRepository, mockRuleBook, mockDisciplineRepository)
        }
        
        given("a competitor to create results for") {

            val weitsprungCategory = CategoryModel(
                    8, "Weitsprung"
            )

            val weitwurfCategory = CategoryModel(
                    8, "Weitwurf"
            )
            
            val birthday = Date(DateTime.now().minusYears(8).millis)
            
            on("creating the results") {

                whenever(mockStarterRepository.save(any<StarterEntity>()))
                        .thenReturn(StarterEntity())
                
                doReturn("2m").whenever(mockRuleBook).run(weitsprungCategory)
                doReturn("5m").whenever(mockRuleBook).run(weitwurfCategory)
                
//                whenever(mockRuleBook.run(weitsprungCategory))
//                        .thenReturn("2m")
//                
//                whenever(mockRuleBook.run(weitwurfCategory))
//                        .thenReturn("5m")
                
                val competitor = CompetitorEntity(1, "Muster", "max", true, birthday)
                
                manager.createResults(competitor)
                
                it("should create a starter") {
                    verify(mockStarterRepository, times(1))
                            .save(any<StarterEntity>())
                }
                
                it("should create a result for each discipline") {
                    verify(mockResultRepository, times(2))
                            .save(any<ResultEntity>())
                }
                
                it("should consider the category rule book to get the distance") {
                    verify(mockRuleBook, times(1))
                            .run(weitsprungCategory)
                    verify(mockRuleBook, times(1))
                            .run(weitwurfCategory)
                }
            }
        }
    }
})