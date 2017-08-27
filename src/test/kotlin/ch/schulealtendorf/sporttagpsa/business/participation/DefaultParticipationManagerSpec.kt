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

import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith

/**
 * @author nmaerchy
 * *
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object DefaultParticipationManagerSpec: Spek({
    
    describe("a participation manager") {
        
        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockParticipationStatus: ParticipationStatus = mock()
        val mockResultManager: ResultManager = mock()
        
        var manager = DefaultParticipationManager(mockParticipationStatus, mockCompetitorRepository, mockResultManager)
        
        beforeEachTest { 
            reset(mockCompetitorRepository, mockParticipationStatus, mockResultManager)
            manager = DefaultParticipationManager(mockParticipationStatus, mockCompetitorRepository, mockResultManager)
        }
        
        given("a participation to finish") {
            
            on("participation status is not finished") {

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(false)
                
                whenever(mockCompetitorRepository.findBySportName(any()))
                        .thenReturn(listOf(CompetitorEntity(), CompetitorEntity()))
                
                manager.finishParticipation()
                
                it("should get all competitors for sport 'Mehrkampf'") {
                    verify(mockCompetitorRepository, times(1))
                            .findBySportName(eq("Mehrkampf"))
                }
                
                it("should create results for each competitor") {
                    verify(mockResultManager, times(2))
                            .createResults(any())
                }
                
                it("should finish the participation status") {
                    verify(mockParticipationStatus, times(1))
                            .finishIt()
                }
            }
            
            on("participation status is finished") {
                
                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(true)
                
                manager.finishParticipation()
                
                it("should do nothing") {
                    verifyZeroInteractions(mockCompetitorRepository)
                    verifyZeroInteractions(mockResultManager)
                }
            }
        }
    }
})
