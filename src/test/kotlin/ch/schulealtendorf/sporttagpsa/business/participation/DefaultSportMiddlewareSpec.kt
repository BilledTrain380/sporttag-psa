/*
 * Copyright (c) 2018 by Nicolas Märchy
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
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.model.SingleParticipant
import ch.schulealtendorf.sporttagpsa.model.Sport
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.platform.runner.JUnitPlatform

/**
 * @author nmaerchy
 * @version 0.0.1
 */
@RunWith(JUnitPlatform::class)
object DefaultSportMiddlewareSpec: Spek({

    describe("a sport middleware") {

        val mockParticipationStatus: ParticipationStatus = mock()
        val mockStarterManager: StarterManager = mock()
        val mockCompetitorRepository: CompetitorRepository = mock()

        var middleware = DefaultSportMiddleware(mockParticipationStatus, mockStarterManager, mockCompetitorRepository)

        beforeEachTest {
            reset(mockParticipationStatus, mockStarterManager, mockCompetitorRepository)
            middleware = DefaultSportMiddleware(mockParticipationStatus, mockStarterManager, mockCompetitorRepository)
        }

        given("a competitor and a sport to accept") {

            on("un-finished participation") {

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(false)


                val participant = SingleParticipant(1, "", "", Gender.male(), "")
                val sport = Sport(1, "")
                middleware.accept(participant, sport)


                it("should skip any logic") {
                    verifyZeroInteractions(mockStarterManager)
                    verifyZeroInteractions(mockCompetitorRepository)
                }
            }

            on("any sport expect Mehrkampf") {

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(true)

                val competitor = CompetitorEntity(1, "Mister", "Max")
                whenever(mockCompetitorRepository.findOne(1))
                        .thenReturn(competitor)


                val participant = SingleParticipant(1, "Muster", "Max", Gender.male(), "")
                val sport = Sport(1, "Schatzsuche")
                middleware.accept(participant, sport)


                it("should remove the starter of the competitor") {
                    verify(mockStarterManager, times(1)).removeStarter(competitor)
                }
            }

            on("non existing starter") {

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(true)

                val competitor = CompetitorEntity(1, "Mister", "Max")
                whenever(mockCompetitorRepository.findOne(1))
                        .thenReturn(competitor)


                val participant = SingleParticipant(1, "Muster", "Max", Gender.male(), "")
                val sport = Sport(1, "Mehrkampf")
                middleware.accept(participant, sport)


                it("should create a starter") {
                    verify(mockStarterManager, times(1)).createStarter(competitor)
                }
            }

            on("already existing starter") {

                whenever(mockParticipationStatus.isFinished())
                        .thenReturn(true)

                val competitor = CompetitorEntity(1, "Mister", "Max")
                whenever(mockCompetitorRepository.findOne(1))
                        .thenReturn(competitor)

                whenever(mockStarterManager.createStarter(competitor))
                        .thenThrow(StarterAlreadyExistsException::class.java)


                val participant = SingleParticipant(1, "Muster", "Max", Gender.male(), "")
                val sport = Sport(1, "Mehrkampf")
                middleware.accept(participant, sport)


                it("should skip the method") {
                    verify(mockStarterManager, times(1)).createStarter(competitor)
                    verifyNoMoreInteractions(mockStarterManager)
                    verify(mockCompetitorRepository, times(1)).findOne(1)
                    verifyNoMoreInteractions(mockCompetitorRepository)
                }
            }
        }
    }

})
