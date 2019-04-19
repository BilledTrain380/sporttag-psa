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

import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.DisciplineDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.psa.dto.UnitDto

internal object RankingFactory {

    fun disciplineRankingOf(competitors: Collection<CompetitorDto>, discipline: DisciplineDto): List<DisciplineRankingDataSet> {

        var rank = 1
        var previousPoints = -1

        return competitors
                .sortedBy { it.resultByDiscipline(discipline).orElse(emptyResult()).points }
                .reversed() // Highest result first
                .mapIndexed { index, competitor ->

                    val result = competitor.resultByDiscipline(discipline).orElse(emptyResult())

                    if (result.points != previousPoints)
                        rank = index + 1

                    previousPoints = result.points

                    DisciplineRankingDataSet(
                            rank,
                            competitor.prename,
                            competitor.surname,
                            competitor.group.name,
                            result.relValue,
                            result.points
                    )
                }
    }

    fun disciplineGroupRankingFactoryOf(competitors: Collection<CompetitorDto>): List<DisciplineGroupRankingDataSet> {

        var rank = 1
        var previousPoints = -1

        return competitors
                .sortedBy { it.results.disciplineGroupTotal() }
                .reversed() // Highest result first
                .mapIndexed { index, competitor ->

                    val totalPoints = competitor.results.disciplineGroupTotal()

                    if (totalPoints != previousPoints) // if total equals previous, the competitor gets the same rank
                        rank = index + 1

                    previousPoints = totalPoints

                    val schnelllauf = competitor.resultByDiscipline("Schnelllauf").orElse(emptyResult())
                    val ballwurf = competitor.resultByDiscipline("Ballwurf").orElse(emptyResult())
                    val weitsprung = competitor.resultByDiscipline("Weitsprung").orElse(emptyResult())

                    DisciplineGroupRankingDataSet(
                            rank,
                            competitor.prename,
                            competitor.surname,
                            competitor.group.name,
                            totalPoints,
                            schnelllauf.relValue,
                            schnelllauf.points,
                            ballwurf.relValue,
                            ballwurf.points,
                            weitsprung.relValue,
                            weitsprung.points
                    )
                }
    }

    fun totalRankingOf(competitors: Collection<CompetitorDto>): List<TotalRankingDataSet> {

        var rank = 1
        var previousPoints = -1

        return competitors
                .sortedBy { it.results.total() }
                .reversed() // Highest result first
                .mapIndexed { index, competitor ->

                    val totalPoints = competitor.results.total()

                    if (totalPoints != previousPoints) // if total equals previous, the competitor gets the same rank
                        rank = index + 1

                    previousPoints = totalPoints

                    val schnelllauf = competitor.resultByDiscipline("Schnelllauf").orElse(emptyResult())
                    val ballwurf = competitor.resultByDiscipline("Ballwurf").orElse(emptyResult())
                    val ballzielwurf = competitor.resultByDiscipline("Ballzielwurf").orElse(emptyResult())
                    val korbeinwurf = competitor.resultByDiscipline("Korbeinwurf").orElse(emptyResult())
                    val seilspringen = competitor.resultByDiscipline("Seilspringen").orElse(emptyResult())
                    val weitsprung = competitor.resultByDiscipline("Weitsprung").orElse(emptyResult())

                    TotalRankingDataSet(
                            rank,
                            competitor.prename,
                            competitor.surname,
                            competitor.group.name,
                            totalPoints,
                            competitor.results.lowest(),
                            schnelllauf.relValue,
                            schnelllauf.points,
                            ballwurf.relValue,
                            ballwurf.points,
                            ballzielwurf.relValue,
                            ballzielwurf.points,
                            korbeinwurf.relValue,
                            korbeinwurf.points,
                            seilspringen.relValue,
                            seilspringen.points,
                            weitsprung.relValue,
                            weitsprung.points
                    )
                }
    }

    private fun List<ResultDto>.total() = this.map { it.points }.sorted().reversed().dropLast(1).sum()

    private fun List<ResultDto>.lowest() = this.map { it.points }.sorted().reversed().last()

    private fun List<ResultDto>.disciplineGroupTotal() = this.filter { it.discipline.name == "Ballwurf" || it.discipline.name == "Schnelllauf" || it.discipline.name == "Weitsprung" }.map { it.points }.sum()

    private fun emptyResult(): ResultDto {
        return ResultDto(
                0,
                0,
                0,
                DisciplineDto("", UnitDto("", 0))
        )
    }
}


