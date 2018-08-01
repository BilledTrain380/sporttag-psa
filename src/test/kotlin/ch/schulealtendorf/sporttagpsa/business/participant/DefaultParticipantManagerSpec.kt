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

package ch.schulealtendorf.sporttagpsa.business.participant

import ch.schulealtendorf.sporttagpsa.business.clazz.ClassManager
import ch.schulealtendorf.sporttagpsa.business.participation.AbsentManager
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import java.util.*

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
object DefaultParticipantManagerSpec: Spek({

    describe("a participant manager") {

        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockClassManager: ClassManager = mock()
        val mockAbsentManager: AbsentManager = mock()

        val manager = DefaultParticipantManager(mockCompetitorRepository, mockClassManager, mockAbsentManager)

        beforeEachTest {
            reset(mockCompetitorRepository, mockClassManager, mockAbsentManager)
//            manager = DefaultParticipantManager(mockCompetitorRepository, mockClassManager, mockAbsentManager)
        }

        context("save a participant") {

            on("a participant with no relation or absent changes") {

                val competitor = CompetitorEntity(1, "Muster", "Max").apply {
                    town = TownEntity(1, "8000", "Zürich")
                    clazz = ClazzEntity("2a", CoachEntity(1, "Sepp"))
                }

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.of(competitor))


                val participant = Participant(
                        1,
                        "Muster",
                        "Willi",
                        Gender.male(),
                        Birthday(0),
                        false,
                        "",
                        Town(1, "8000", "Zürich"),
                        Clazz("2a", Coach(1,"Sepp")))

                manager.saveParticipant(participant)


                it("should save the participant") {
                    val expected = competitor.copy().apply {
                        prename = "Willi"
                    }
                    verify(mockCompetitorRepository, times(1)).save(expected)
                }
            }

            on("a participant with a changed relations") {

                val competitor = CompetitorEntity(1, "Muster", "Max").apply {
                    town = TownEntity(1, "8000", "Zürich")
                    clazz = ClazzEntity("2a", CoachEntity(1, "Sepp"))
                }

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.of(competitor))


                val participant = Participant(
                        1,
                        "Muster",
                        "Max",
                        Gender.male(),
                        Birthday(0),
                        false,
                        "",
                        Town(2, "3000", "Bern"),
                        Clazz("3a", Coach(2, "Müller")))

                manager.saveParticipant(participant)


                it("should update the class relation") {
                    val expected = competitor.copy().apply {
                        clazz = ClazzEntity("3a", CoachEntity(2, "Müller"))
                        town = TownEntity(2, "3000", "Bern")
                    }
                    verify(mockCompetitorRepository, times(1)).save(expected)
                }
            }

            on("a participant with changed sport relation") {

                val competitor = CompetitorEntity(1, "Muster", "Max")

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.of(competitor))


                val participant = Participant(
                        1,
                        "Muster",
                        "Willi",
                        Gender.male(),
                        Birthday(0),
                        false,
                        "",
                        Town(1, "8000", "Zürich"),
                        Clazz("2a", Coach(1,"Sepp")),
                        Optional.of("Skipping"))

                manager.saveParticipant(participant)

                it("should update the sport relation") {
                    val expected = competitor.copy().apply {
                        sport = SportEntity("Skipping")
                    }
                    verify(mockCompetitorRepository, times(1)).save(expected)
                }
            }

            on("a participant which is marked as absent") {

                val competitor = CompetitorEntity(1, "Muster", "Max").apply {
                    town = TownEntity(1, "8000", "Zürich")
                    clazz = ClazzEntity("2a", CoachEntity(1, "Sepp"))
                }

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.of(competitor))


                val participant = Participant(
                        1,
                        "Muster",
                        "Max",
                        Gender.male(),
                        Birthday(0),
                        true,
                        "",
                        Town(1, "8000", "Zürich"),
                        Clazz("2a", Coach(1,"Sepp")))

                manager.saveParticipant(participant)


                val verifyOrder = inOrder(mockCompetitorRepository, mockAbsentManager)

                it("should save the participant before marking it as absent") {
                    verifyOrder.verify(mockCompetitorRepository, times(1)).save(competitor)
                }

                it("should mark the given participant as absent") {
                    verifyOrder.verify(mockAbsentManager, times(1)).markAsAbsent(competitor)
                }
            }

            on("a participant which is marked as present") {

                val competitor = CompetitorEntity(1, "Muster", "Max").apply {
                    town = TownEntity(1, "8000", "Zürich")
                    clazz = ClazzEntity("2a", CoachEntity(1, "Sepp"))
                }

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.of(competitor))


                val participant = Participant(
                        1,
                        "Muster",
                        "Max",
                        Gender.male(),
                        Birthday(0),
                        false,
                        "",
                        Town(1, "8000", "Zürich"),
                        Clazz("2a", Coach(1,"Sepp")))

                manager.saveParticipant(participant)


                val verifyOrder = inOrder(mockCompetitorRepository, mockAbsentManager)

                it("should save the participant before marking it as present") {
                    verifyOrder.verify(mockCompetitorRepository, times(1)).save(competitor)
                }

                it("should mark the given participant as absent") {
                    verifyOrder.verify(mockAbsentManager, times(1)).markAsPresent(competitor)
                }
            }

            on("a participant which does not exist") {

                whenever(mockCompetitorRepository.findById(any())).thenReturn(Optional.empty())


                val participant = Participant(
                        1,
                        "Muster",
                        "Max",
                        Gender.male(),
                        Birthday(0),
                        false,
                        "",
                        Town(1, "8000", "Zürich"),
                        Clazz("2a", Coach(1,"Sepp")))

                manager.saveParticipant(participant)

                it("should create a new participant") {
                    val expected = CompetitorEntity(null, "Muster", "Max").apply {
                        town = TownEntity(1, "8000", "Zürich")
                        clazz = ClazzEntity("2a", CoachEntity(1, "Sepp"))
                    }
                    verify(mockCompetitorRepository, times(1)).save(expected)
                    verify(mockAbsentManager, times(1)).markAsPresent(expected)
                }
            }
        }
    }
})
