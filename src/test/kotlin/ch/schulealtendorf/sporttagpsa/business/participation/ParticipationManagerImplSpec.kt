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

import ch.schulealtendorf.sporttagpsa.business.rulebook.CategoryRuleBook
import ch.schulealtendorf.sporttagpsa.entity.*
import ch.schulealtendorf.sporttagpsa.model.*
import ch.schulealtendorf.sporttagpsa.repository.*
import com.nhaarman.mockito_kotlin.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
object ParticipationManagerImplSpec: Spek({

    describe("a participation manager") {

        val mockParticipantRepository: ParticipantRepository = mock()
        val mockAbsentRepository: AbsentParticipantRepository = mock()
        val mockParticipationRepository: ParticipationRepository = mock()
        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockCategoryBook: CategoryRuleBook = mock()
        val mockDisciplineRepository: DisciplineRepository = mock()

        val manager = ParticipationManagerImpl(
                mockParticipantRepository,
                mockAbsentRepository,
                mockParticipationRepository,
                mockCompetitorRepository,
                mockCategoryBook,
                mockDisciplineRepository,
                mock()
        )

        beforeEachTest {
            reset(
                    mockParticipantRepository,
                    mockAbsentRepository,
                    mockParticipationRepository,
                    mockCompetitorRepository,
                    mockCategoryBook,
                    mockDisciplineRepository
            )
        }

        val townEntity = TownEntity(
                id = 1,
                zip = "3000",
                name = "Bern"
        )

        val town = Town(
                zip = "3000",
                name = "Bern"
        )

        val groupEntity = GroupEntity(
                name = "2a",
                coach = CoachEntity(
                        id = 1,
                        name = "Willi"
                )
        )

        val group = Group(
                name = "2a",
                coach = Coach(
                        id = 1,
                        name = "Willi"
                )
        )

        val participantEntity = ParticipantEntity(
                id = 1,
                surname = "Muster",
                prename = "Max",
                gender = "MALE",
                birthday = 0,
                address = "Musterstrasse 8",
                town = townEntity,
                group = groupEntity
        )

        val participantModel = Participant(
                1,
                "Muster",
                "Max",
                Gender.MALE,
                Birthday(0),
                false,
                "Musterstrasse 8",
                town,
                group
        )

        context("mark as absent") {

            on("present participant") {

                whenever(mockAbsentRepository.findByParticipantId(any())).thenReturn(Optional.empty())
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))


                manager.markAsAbsent(participantModel)


                it("should create a absent participant entry") {
                    val expected = AbsentParticipantEntity(
                            participant = participantEntity.copy()
                    )
                    verify(mockAbsentRepository, times(1)).save(expected)
                }
            }

            on("already absent participant") {

                whenever(mockAbsentRepository.findByParticipantId(any())).thenReturn(Optional.of(AbsentParticipantEntity(1, participantEntity.copy())))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))


                manager.markAsAbsent(participantModel)


                it("should not create or update any entities") {
                    verify(mockAbsentRepository, times(1)).findByParticipantId(any())
                    verifyNoMoreInteractions(mockAbsentRepository)
                }
            }

            on("participant does not exist") {

                whenever(mockAbsentRepository.findByParticipantId(any())).thenReturn(Optional.empty())


                it("should throw a now such element exception indicating, that the given participant could not be found") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.markAsAbsent(participantModel)
                    }
                    assertEquals("Could not found participant: id=1", exception.message)
                }
            }
        }

        context("mark as present") {

            on("absent participant") {

                whenever(mockParticipantRepository.existsById(any())).thenReturn(true)

                val absentParticipant = AbsentParticipantEntity(1, participantEntity.copy())
                whenever(mockAbsentRepository.findByParticipantId(any())).thenReturn(Optional.of(absentParticipant))


                manager.markAsPresent(participantModel)


                it("should remove the absent participant entry") {
                    verify(mockAbsentRepository, times(1)).delete(absentParticipant)
                }
            }

            on("already present paritcipant") {

                whenever(mockParticipantRepository.existsById(any())).thenReturn(true)
                whenever(mockAbsentRepository.findByParticipantId(any())).thenReturn(Optional.empty())


                manager.markAsPresent(participantModel)


                it("should not create or update any entities") {
                    verify(mockAbsentRepository, times(1)).findByParticipantId(any())
                    verifyNoMoreInteractions(mockAbsentRepository)
                }
            }

            on("participant does not exist") {

                whenever(mockParticipantRepository.existsById(any())).thenReturn(false)


                it("should throw a now such element exception indicating, that the given participant could not be found") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.markAsPresent(participantModel)
                    }
                    assertEquals("Could not found participant: id=1", exception.message)
                }
            }
        }

        context("participate") {

            on("open participation") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "OPEN")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))


                manager.participate(participantModel, Sport("athletics"))


                it("should set the sport to the participant") {
                    val expected = participantEntity.copy(sport = SportEntity("athletics"))
                    verify(mockParticipantRepository, times(1)).save(expected)
                }
            }

            on("participant could not be found") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "OPEN")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.empty())


                it("should throw a no such element exception, indicating that the participant does not exist") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.participate(participantModel, Sport(""))
                    }
                    assertEquals("Could not find participant: id=1", exception.message)
                }
            }

            on("closed participation") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))


                it("should throw an illegal state exception, indicating that this operation can not be performed") {
                    val exception = assertFailsWith<IllegalStateException> {
                        manager.participate(participantModel, Sport(""))
                    }
                    assertEquals("Participation already closed. Use ParticipationManager#reParticipate instead", exception.message)
                }
            }
        }

        context("re-participate") {

            on("changing sport from any other to athletics") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))

                val disciplines = listOf(
                        DisciplineEntity("soccer", UnitEntity("m")),
                        DisciplineEntity("running", UnitEntity("sec")),
                        DisciplineEntity("long-jump", UnitEntity("m"))
                )
                whenever(mockDisciplineRepository.findAll()).thenReturn(disciplines)

                whenever(mockCategoryBook.getDistance(any())).thenReturn(null)
                val competitor = CompetitorEntity(
                        participant = participantEntity.copy()
                )
                whenever(mockCompetitorRepository.save(any<CompetitorEntity>())).thenReturn(competitor)


                manager.reParticipate(participantModel, Sport(LockedSport.ATHLETICS))


                it("should save the participant as competitor with default results") {
                    verify(mockCompetitorRepository, times(1)).save(competitor)
                    verify(mockCompetitorRepository, times(1)).save(competitor.apply {
                        results = disciplines.map { ResultEntity(discipline = it).also { it.competitor = competitor } }.toSet()
                    })
                }

                it("should consider the category") {
                    verify(mockCategoryBook, times(3)).getDistance(any())
                }

                it("should change the sport to athletics") {
                    val expected = participantEntity.copy(sport = SportEntity(LockedSport.ATHLETICS))
                    verify(mockParticipantRepository, times(1)).save(expected)
                }
            }

            on("changing sport from athletics to any other") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))

                val competitor = CompetitorEntity( // we only set some sample values, a real competitor would have results
                        1,
                        participantEntity.copy()
                )
                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.of(competitor))


                manager.reParticipate(participantModel, Sport("bike"))


                it("should remove the competitor entry") {
                    verify(mockCompetitorRepository, times(1)).delete(competitor)
                }

                it("should change the sport") {
                    val expected = participantEntity.copy(sport = SportEntity("bike"))
                    verify(mockParticipantRepository, times(1)).save(expected)
                }
            }

            on("open participation") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "OPEN")))


                it("should throw an illegal state exception, indicating that this operation can not be performed") {
                    val exception = assertFailsWith<IllegalStateException> {
                        manager.reParticipate(participantModel, Sport(""))
                    }
                    assertEquals("Participation is open. Use ParticipationManager#participate instead", exception.message)
                }
            }

            on("same sport as the participant already has") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy(sport = SportEntity("running"))))


                val sport = Sport("running")
                val participant = participantModel.copy(sport = Optional.of(sport))
                manager.reParticipate(participant, sport)


                it("should not update or create any data") {
                    verify(mockParticipantRepository, times(1)).findById(any())
                    verifyNoMoreInteractions(mockParticipantRepository)
                    verifyZeroInteractions(mockCompetitorRepository)
                }
            }

            on("changing sport from athletics to any other, but the participant is not a competitor") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))
                whenever(mockParticipantRepository.findById(any())).thenReturn(Optional.of(participantEntity.copy()))
                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.empty())


                manager.reParticipate(participantModel, Sport("running"))


                it("should not remove any competitor") {
                    verify(mockCompetitorRepository, times(1)).findByParticipantId(any())
                    verifyNoMoreInteractions(mockCompetitorRepository)
                }
            }
        }

        context("close participation") {

            on("open participation") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "OPEN")))

                val participant1 = participantEntity.copy(sport = SportEntity(LockedSport.ATHLETICS))
                val participant2 = participantEntity.copy(id = 2, sport = SportEntity(LockedSport.ATHLETICS)) // we just change the id, that is enough

                whenever(mockParticipantRepository.findBySportName(LockedSport.ATHLETICS)).thenReturn(listOf(participant1, participant2))

                val disciplines = listOf(
                        DisciplineEntity("soccer", UnitEntity("m")),
                        DisciplineEntity("running", UnitEntity("sec")),
                        DisciplineEntity("long-jump", UnitEntity("m"))
                )
                whenever(mockDisciplineRepository.findAll()).thenReturn(disciplines)

                val competitor1 = CompetitorEntity(
                        participant = participant1.copy()
                )
                val competitor2 = CompetitorEntity(
                        participant = participant2.copy()
                )
                whenever(mockCompetitorRepository.save(competitor1)).thenReturn(competitor1.copy())
                whenever(mockCompetitorRepository.save(competitor2)).thenReturn(competitor2.copy())


                manager.closeParticipation()


                it("should create competitors with default results for each participant with sport athletics") {
                    /*
                      * 1. the competitor without results needs to be saved
                      * 2. the results with the competitor reference can be se
                      * 3. the competitor including the results must be saved
                      */

                    verify(mockCompetitorRepository, times(1)).save(competitor1)
                    verify(mockCompetitorRepository, times(1)).save(competitor1.apply {
                        results = disciplines.map { ResultEntity(discipline = it).also { it.competitor = competitor1 } }.toSet()
                    })

                    verify(mockCompetitorRepository, times(1)).save(competitor2)
                    verify(mockCompetitorRepository, times(1)).save(competitor2.apply {
                        results = disciplines.map { ResultEntity(discipline = it).also { it.competitor = competitor2 } }.toSet()
                    })
                }

                it("should close the participation") {
                    verify(mockParticipationRepository, times(1)).save(ParticipationEntity(MAIN_PARTICIPATION, "CLOSE"))
                }
            }

            on("already closed participation") {

                whenever(mockParticipationRepository.findById(any())).thenReturn(Optional.of(ParticipationEntity("main", "CLOSE")))


                manager.closeParticipation()


                it("should not perform any interactions") {
                    verify(mockParticipationRepository, times(1)).findById(MAIN_PARTICIPATION)
                    verifyNoMoreInteractions(mockParticipantRepository)
                    verifyZeroInteractions(mockCompetitorRepository)
                    verifyZeroInteractions(mockParticipantRepository)
                }
            }
        }
    }
})
