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

import ch.schulealtendorf.sporttagpsa.entity.AbsentCompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.AbsentCompetitorRepository
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
import java.util.*
import kotlin.test.assertEquals


@RunWith(JUnitPlatform::class)
object DefaultParticipationManagerSpec: Spek({
    
    describe("a participation manager") {
        
        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockSportRepository: SportRepository = mock()
        val mockAbsentCompetitorRepository: AbsentCompetitorRepository = mock()
        val mockSportMiddleware: SportMiddleware = mock()
        
        var manager = DefaultParticipationManager(mockCompetitorRepository, mockSportRepository, mockAbsentCompetitorRepository, mockSportMiddleware)
        
        beforeEachTest { 
            reset(mockCompetitorRepository, mockSportRepository, mockAbsentCompetitorRepository, mockSportMiddleware)
            manager = DefaultParticipationManager(mockCompetitorRepository, mockSportRepository, mockAbsentCompetitorRepository, mockSportMiddleware)
        }

        given("a clazz to get participants") {

            on("present participant") {

                whenever(mockAbsentCompetitorRepository.findAll()).thenReturn(listOf())

                whenever(mockCompetitorRepository.findByClazzId(1)).thenReturn(
                        listOf(
                                CompetitorEntity(1, "Muster", "Max", true, 0, "")
                        )
                )


                it("should map the participant as present") {
                    val expected = Participant(1, "Muster", "Max", Gender(true), Birthday(0), "", false, Optional.empty())
                    assertEquals(listOf(expected), manager.getParticipantListByClazz(Clazz(1, "2a", "")))
                }
            }

            on("absent participant") {

                val competitor = CompetitorEntity(1, "Muster", "Max", true, 0, "")

                whenever(mockAbsentCompetitorRepository.findAll()).thenReturn(listOf(AbsentCompetitorEntity(1, competitor)))

                whenever(mockCompetitorRepository.findByClazzId(1)).thenReturn(
                        listOf(
                                CompetitorEntity(1, "Muster", "Max", true, 0, "")
                        )
                )


                it("should map the participant as absent") {
                    val expected = Participant(1, "Muster", "Max", Gender(true), Birthday(0), "", true, Optional.empty())
                    assertEquals(listOf(expected), manager.getParticipantListByClazz(Clazz(1, "2a", "")))
                }
            }
        }

        given("a participant to mark as absent") {

            on("marked as present") {

                val competitor = CompetitorEntity(1, "Muster", "Max", true, 0, "")

                whenever(mockCompetitorRepository.findOne(1)).thenReturn(competitor)

                whenever(mockAbsentCompetitorRepository.findAll()).thenReturn(listOf())


                val singleParticipant = SingleParticipant(1, "Muster", "Max", Gender(true), "")
                manager.markAsAbsent(singleParticipant)


                it("should create an absent competitor") {
                    val expected = AbsentCompetitorEntity(null, competitor)
                    verify(mockAbsentCompetitorRepository, times(1)).save(expected)
                }
            }

            on("already marked as absent") {

                val competitor = CompetitorEntity(1, "Muster", "Max", true, 0, "")

                whenever(mockCompetitorRepository.findOne(1)).thenReturn(competitor)

                whenever(mockAbsentCompetitorRepository.findAll()).thenReturn(
                        listOf(
                                AbsentCompetitorEntity(1, competitor)
                        )
                )


                val singleParticipant = SingleParticipant(1, "Muster", "Max", Gender(true), "")
                manager.markAsAbsent(singleParticipant)


                it("should not create any absent competitor") {
                    verify(mockAbsentCompetitorRepository, times(1)).findAll()
                    verifyNoMoreInteractions(mockAbsentCompetitorRepository)
                }
            }
        }

        given("a participant to mark as present") {

            on("marked as absent") {

                val absentCompetitor = AbsentCompetitorEntity(1, CompetitorEntity(1, "Muster", "Max", true, 0, ""))

                whenever(mockAbsentCompetitorRepository.findByCompetitorId(1)).thenReturn(absentCompetitor)


                val singleParticipant = SingleParticipant(1, "Muster", "Max", Gender(true), "")
                manager.markAsPresent(singleParticipant)


                it("should delete the according absent competitor") {
                    verify(mockAbsentCompetitorRepository, times(1)).delete(absentCompetitor)
                }
            }
        }
    }
})
