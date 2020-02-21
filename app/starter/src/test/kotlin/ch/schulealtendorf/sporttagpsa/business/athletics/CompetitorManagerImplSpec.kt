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

package ch.schulealtendorf.sporttagpsa.business.athletics

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.DisciplineDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.psa.dto.UnitDto
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.entity.DisciplineEntity
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

object CompetitorManagerImplSpec : Spek({

    describe("a competitor manager") {

        val mockCompetitorRepository: CompetitorRepository = mock()
        val mockDisciplineRepository: DisciplineRepository = mock()

        val manager = CompetitorManagerImpl(mockCompetitorRepository, mockDisciplineRepository)

        beforeEachTest {
            reset(mockCompetitorRepository, mockDisciplineRepository)
        }

        // just a test instance which we copy for the specs
        val competitor = CompetitorDto(
                1,
                1,
                "",
                "",
                GenderDto.MALE,
                BirthdayDto(0),
                false,
                "",
                TownDto("", ""),
                GroupDto("", CoachDto(1, "")),
                listOf())

        context("results to merge") {

            on("new results") {

                // discipline does not matter here, we only verify that the results are merged
                val results = listOf(
                        ResultDto(1, 50, 800, DisciplineDto("", UnitDto("", 1), false, false)),
                        ResultDto(2, 49, 795, DisciplineDto("", UnitDto("", 1), false, false)),
                        ResultDto(3, 45, 846, DisciplineDto("", UnitDto("", 1), false, false))
                )


                val result = manager.mergeResults(competitor, results)


                it("should add the new results") {
                    val expected = competitor.copy(results = results)
                    assertEquals(expected, result)
                }
            }

            on("existing results") {

                // discipline does not matter here, we only verify that the results are merged
                val results = listOf(
                        ResultDto(1, 50, 800, DisciplineDto("", UnitDto("", 1), false, false)),
                        ResultDto(2, 49, 795, DisciplineDto("", UnitDto("", 1), false, false))
                )


                val existingResults = listOf(
                        ResultDto(1, 20, 122, DisciplineDto("", UnitDto("", 1), false, false)),
                        ResultDto(2, 21, 158, DisciplineDto("", UnitDto("", 1), false, false)),
                        ResultDto(3, 45, 456, DisciplineDto("", UnitDto("", 1), false, false))
                )
                val result = manager.mergeResults(competitor.copy(results = existingResults), results)


                it("should update the existing results") {
                    val expected = competitor.copy(results = results.plus(existingResults[2]))
                    assertEquals(expected, result)
                }
            }
        }

        context("results to update") {

            val competitorEntity = CompetitorEntity()

            on("new results") {

                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.of(competitorEntity))
                whenever(mockDisciplineRepository.findById("test")).thenReturn(Optional.of(DisciplineEntity("test")))


                val results = listOf(
                        ResultDto(0, 20, 122, DisciplineDto("test", UnitDto("", 1), false, false)),
                        ResultDto(0, 21, 158, DisciplineDto("test", UnitDto("", 1), false, false)),
                        ResultDto(0, 45, 456, DisciplineDto("test", UnitDto("", 1), false, false))
                )
                manager.saveCompetitorResults(competitor.copy(results = results))


                it("should get the disciplines of the new results") {
                    verify(mockDisciplineRepository, times(3)).findById("test")
                }

                it("should create the new results") {
                    val expected = setOf(
                            ResultEntity(value = 20, points = 122, discipline = DisciplineEntity("test")),
                            ResultEntity(value = 21, points = 158, discipline = DisciplineEntity("test")),
                            ResultEntity(value = 45, points = 456, discipline = DisciplineEntity("test"))
                    )
                    verify(mockCompetitorRepository, times(1)).save(competitorEntity.copy(results = expected))
                }
            }

            on("existing results") {

                val existingResults = setOf(
                        ResultEntity(id = 1, value = 20, points = 122, discipline = DisciplineEntity("test")),
                        ResultEntity(id = 2, value = 21, points = 158, discipline = DisciplineEntity("test"))
                )
                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.of(competitorEntity.copy(results = existingResults)))
                whenever(mockDisciplineRepository.findById("test")).thenReturn(Optional.of(DisciplineEntity("test")))


                val results = listOf(
                        ResultDto(1, 25, 133, DisciplineDto("test", UnitDto("", 1), false, false)),
                        ResultDto(2, 12, 102, DisciplineDto("test", UnitDto("", 1), false, false)),
                        ResultDto(0, 45, 456, DisciplineDto("test", UnitDto("", 1), false, false))
                )
                manager.saveCompetitorResults(competitor.copy(results = results))


                it("should update the existing results") {
                    val expected = setOf(
                            ResultEntity(id = 1, value = 25, points = 133, discipline = DisciplineEntity("test")),
                            ResultEntity(id = 2, value = 12, points = 102, discipline = DisciplineEntity("test")),
                            ResultEntity(value = 45, points = 456, discipline = DisciplineEntity("test"))
                    )
                    verify(mockCompetitorRepository, times(1)).save(competitorEntity.copy(results = expected))
                }
            }

            on("competitor not found") {

                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.empty())


                it("should throw a no such element exception, indicating that the competitor does not exist") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.saveCompetitorResults(competitor)
                    }
                    assertEquals("Could not find competitor: id=1", exception.message)
                }
            }

            on("disciplineDto not found") {

                whenever(mockCompetitorRepository.findByParticipantId(any())).thenReturn(Optional.of(competitorEntity))
                whenever(mockDisciplineRepository.findById("test")).thenReturn(Optional.empty())


                val results = listOf(
                        ResultDto(0, 20, 122, DisciplineDto("test", UnitDto("", 1), false, false)),
                        ResultDto(0, 21, 158, DisciplineDto("test", UnitDto("", 1), false, false))
                )


                it("should throw a no such element exception, indicating that the discipline does not exist") {
                    val exception = assertFailsWith<NoSuchElementException> {
                        manager.saveCompetitorResults(competitor.copy(results = results))
                    }
                    assertEquals("Could not find discipline: name=test", exception.message)
                }
            }
        }
    }
})
