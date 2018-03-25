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

import ch.schulealtendorf.sporttagpsa.business.participation.ParticipationStatus
import ch.schulealtendorf.sporttagpsa.controller.model.SimpleCompetitorModel
import ch.schulealtendorf.sporttagpsa.controller.model.SportModel
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.SportRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object DefaultCompetitorProviderSpec: Spek({
    
    describe("a competitor provider") {

        val mockCompetitorRepo: CompetitorRepository = mock()
        val mockSportRepo: SportRepository = mock()
        val mockParticipationStatus: ParticipationStatus = mock()

        var provider: DefaultCompetitorProvider = DefaultCompetitorProvider(mockCompetitorRepo, mockSportRepo, mockParticipationStatus)

        beforeEachTest {
            reset(mockCompetitorRepo, mockSportRepo, mockParticipationStatus)
            provider = DefaultCompetitorProvider(mockCompetitorRepo, mockSportRepo, mockParticipationStatus)
        }
        
        given("a competitor model to update") {

            val townEntity = TownEntity(1, "8000", "Musterhausen")
            val clazzEntity = ClazzEntity(1, "1a", TeacherEntity(1, "teacher"))
            val sportEntity = SportEntity(1, "Brennball")

            on("selected sport") {

                val competitorModel = SimpleCompetitorModel(1, "Wirbelwind", "Will", false, "address", SportModel(1))

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(false)
                
                whenever(mockCompetitorRepo.findOne(any()))
                        .thenReturn(CompetitorEntity(1, "Wirbelwind", "Will", false, 1, "address", townEntity, clazzEntity, null))
                
                whenever(mockSportRepo.findOne(any()))
                        .thenReturn(sportEntity)

                provider.updateCompetitor(competitorModel)

                it("should update the CompetitorEntity with the according SportEntity") {
                    val expected: CompetitorEntity = CompetitorEntity(1, "Wirbelwind", "Will", false, 1, "address", townEntity, clazzEntity, sportEntity)
                    Mockito.verify(mockCompetitorRepo, Mockito.times(1)).save(expected)
                }
            }

            on("non selected sport") {

                val competitorModel: SimpleCompetitorModel = SimpleCompetitorModel(1, "Wirbelwind", "Will", false, "address", SportModel(0))

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(false)
                
                whenever(mockCompetitorRepo.findOne(any()))
                        .thenReturn(CompetitorEntity(1, "Wirbelwind", "Will", false, 1, "address", townEntity, clazzEntity, null))
                
                whenever(mockSportRepo.findOne(any()))
                        .thenReturn(null)

                provider.updateCompetitor(competitorModel)

                it("should update the CompetitorEntity with no SportEntity") {
                    val expected: CompetitorEntity = CompetitorEntity(1, "Wirbelwind", "Will", false, 1, "address", townEntity, clazzEntity, null)
                    Mockito.verify(mockCompetitorRepo, Mockito.times(1)).save(expected)
                }
            }
            
            on("finished participation") {
                
                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(true)
                
                provider.updateCompetitor(SimpleCompetitorModel())
                
                it("should do nothing") {
                    verifyZeroInteractions(mockCompetitorRepo)
                    verifyZeroInteractions(mockSportRepo)
                }
            }
        }
    }
})