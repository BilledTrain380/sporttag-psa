/*
 * Copyright (c) 2019 by Nicolas Märchy
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

package ch.schulealtendorf.psa.shared.reporting.ranking

import ch.schulealtendorf.psa.dto.BirthdayDto
import ch.schulealtendorf.psa.dto.CoachDto
import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.DisciplineDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.dto.GroupDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.psa.dto.TownDto
import ch.schulealtendorf.psa.dto.UnitDto
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

object RankingFactorySpec : Spek({

    describe("RankingFactory") {

        describe("discipline group ranking") {

            given("a competitor list") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Schnelllauf"),
                                resultDtoOf(points = 100, discipline = "Ballwurf"),
                                resultDtoOf(points = 100, discipline = "Weitsprung")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 300, discipline = "Schnelllauf"),
                                resultDtoOf(points = 300, discipline = "Ballwurf"),
                                resultDtoOf(points = 300, discipline = "Weitsprung")
                        )),
                        competitorDtoOf(surname = "2. rank", results = listOf(
                                resultDtoOf(points = 200, discipline = "Schnelllauf"),
                                resultDtoOf(points = 200, discipline = "Ballwurf"),
                                resultDtoOf(points = 200, discipline = "Weitsprung")
                        ))
                )

                val ranking = RankingFactory.disciplineGroupRankingFactoryOf(competitors)

                it("should order by the rank") {
                    val expected = listOf("1. rank", "2. rank", "3. rank")
                    assertEquals(expected, ranking.map { it.surname })
                }
            }

            given("a competitor list when competitors have the same points") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Schnelllauf"),
                                resultDtoOf(points = 100, discipline = "Ballwurf"),
                                resultDtoOf(points = 100, discipline = "Weitsprung")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 300, discipline = "Schnelllauf"),
                                resultDtoOf(points = 300, discipline = "Ballwurf"),
                                resultDtoOf(points = 300, discipline = "Weitsprung")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 300, discipline = "Schnelllauf"),
                                resultDtoOf(points = 300, discipline = "Ballwurf"),
                                resultDtoOf(points = 300, discipline = "Weitsprung")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 300, discipline = "Schnelllauf"),
                                resultDtoOf(points = 300, discipline = "Ballwurf"),
                                resultDtoOf(points = 300, discipline = "Weitsprung")
                        ))
                )

                val ranking = RankingFactory.disciplineGroupRankingFactoryOf(competitors)

                it("should give them the same rank") {
                    assertEquals(ranking[0].rank, 1)
                    assertEquals(ranking[1].rank, 1)
                    assertEquals(ranking[2].rank, 1)
                }

                it("should skip the next rank") {
                    assertEquals(ranking[3].rank, 4)
                }
            }
        }

        describe("discipline ranking") {

            given("a competitor list") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Ballzielwurf")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 300, discipline = "Ballzielwurf")
                        )),
                        competitorDtoOf(surname = "2. rank", results = listOf(
                                resultDtoOf(points = 200, discipline = "Ballzielwurf")
                        ))
                )

                val discipline = DisciplineDto("Ballzielwurf", UnitDto("", 0))
                val ranking = RankingFactory.disciplineRankingOf(competitors, discipline)

                it("should order by the rank") {
                    val expected = listOf("1. rank", "2. rank", "3. rank")
                    assertEquals(expected, ranking.map { it.surname })
                }
            }

            given("a competitor list when competitors have the same points") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. Rank", results = listOf(
                                resultDtoOf(points = 50, discipline = "Ballzielwurf")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Ballzielwurf")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Ballzielwurf")
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100, discipline = "Ballzielwurf")
                        ))
                )

                val discipline = DisciplineDto("Ballzielwurf", UnitDto("", 0))
                val ranking = RankingFactory.disciplineRankingOf(competitors, discipline)

                it("should give them the same rank") {
                    assertEquals(ranking[0].rank, 1)
                    assertEquals(ranking[1].rank, 1)
                    assertEquals(ranking[2].rank, 1)
                }

                it("should skip the next rank") {
                    assertEquals(ranking[3].rank, 4)
                }
            }
        }

        describe("total ranking") {
            given("a competitor list") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 100)
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 200),
                                resultDtoOf(points = 300)
                        )),
                        competitorDtoOf(surname = "2. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 150),
                                resultDtoOf(points = 300)
                        ))
                )

                val ranking = RankingFactory.totalRankingOf(competitors)


                it("should not count the weakest result to the total points") {
                    assertEquals(500, ranking[0].total)
                }

                it("should set the weakest result as deleted result") {
                    assertEquals(100, ranking[0].deletedResult)
                }

                it("should order by the rank") {
                    val expected = listOf("1. rank", "2. rank", "3. rank")
                    assertEquals(expected, ranking.map { it.surname })
                }
            }

            given("a competitor list where competitors have the same total points") {

                val competitors = listOf(
                        competitorDtoOf(surname = "3. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 100)
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 200),
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 300)
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 200),
                                resultDtoOf(points = 300)
                        )),
                        competitorDtoOf(surname = "1. rank", results = listOf(
                                resultDtoOf(points = 100),
                                resultDtoOf(points = 200),
                                resultDtoOf(points = 300)
                        ))
                )

                val ranking = RankingFactory.totalRankingOf(competitors)

                it("should give them the same rank") {
                    assertEquals(ranking[0].rank, 1)
                    assertEquals(ranking[1].rank, 1)
                    assertEquals(ranking[2].rank, 1)
                }

                it("should skip the next rank") {
                    assertEquals(ranking[3].rank, 4)
                }
            }
        }
    }
})

internal fun competitorDtoOf(
        id: Int = 1,
        startNumber: Int = 1,
        surname: String = "",
        prename: String = "",
        gender: GenderDto = GenderDto.MALE,
        birthday: BirthdayDto = BirthdayDto(0),
        absent: Boolean = false,
        address: String = "",
        zip: String = "",
        town: String = "",
        group: String = "",
        coach: String = "",
        results: List<ResultDto> = listOf()
) = CompetitorDto(id, startNumber, surname, prename, gender, birthday, absent, address, TownDto(zip, town), GroupDto(group, CoachDto(1, coach)), results)

internal fun resultDtoOf(
        id: Int = 1,
        value: Long = 0,
        points: Int = 0,
        discipline: String = ""
) = ResultDto(id, value, points, DisciplineDto(discipline, UnitDto("", 0)), "")