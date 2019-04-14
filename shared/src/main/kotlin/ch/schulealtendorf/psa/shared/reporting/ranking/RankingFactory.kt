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

internal class RankingFactory {

    fun disciplineRankingOf(competitors: Collection<CompetitorDto>, discipline: DisciplineDto): List<DisciplineRankingDataSet> {

        var rank = 1
        var previousPoints = -1

        return competitors
                .sortedBy { it.results.find(discipline).points }
                .reversed() // Highest result first
                .mapIndexed { index, competitor ->

                    val result = competitor.results.find(discipline)

                    if (result.points != previousPoints)
                        rank = index + 1

                    previousPoints = result.points

                    DisciplineRankingDataSet(
                            rank,
                            competitor.prename,
                            competitor.surname,
                            competitor.group.name,
                            result.asText(),
                            result.points
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

                    val schnelllauf = competitor.results.find { it.discipline.name == "Schnelllauf" }
                            ?: emptyResult()
                    val ballwurf = competitor.results.find { it.discipline.name == "Ballwurf" } ?: emptyResult()
                    val ballzielwurf = competitor.results.find { it.discipline.name == "Ballzielwurf" }
                            ?: emptyResult()
                    val korbeinwurf = competitor.results.find { it.discipline.name == "Korbeinwurf" }
                            ?: emptyResult()
                    val seilspringen = competitor.results.find { it.discipline.name == "Seilspringen" }
                            ?: emptyResult()
                    val weitsprung = competitor.results.find { it.discipline.name == "Wecompetitorsprung" }
                            ?: emptyResult()

                    TotalRankingDataSet(
                            rank,
                            competitor.prename,
                            competitor.surname,
                            competitor.group.name,
                            totalPoints,
                            competitor.results.lowest(),
                            schnelllauf.asText(),
                            schnelllauf.points,
                            ballwurf.asText(),
                            ballwurf.points,
                            ballzielwurf.asText(),
                            ballzielwurf.points,
                            korbeinwurf.asText(),
                            korbeinwurf.points,
                            seilspringen.asText(),
                            seilspringen.points,
                            weitsprung.asText(),
                            weitsprung.points
                    )
                }
    }

    private fun List<ResultDto>.find(discipline: DisciplineDto) = this.find { it.discipline == discipline }
            ?: emptyResult()

    private fun List<ResultDto>.total() = this.map { it.points }.sorted().reversed().dropLast(1).sum()

    private fun List<ResultDto>.lowest() = this.map { it.points }.sorted().reversed().last()

    private fun ResultDto.asText(): String {

        val value: Double = value.toDouble() / discipline.unit.factor

        if (value % 1 == 0.0) {
            return value.toInt().toString()
        }

        return value.toString()
    }

    private fun emptyResult(): ResultDto {
        return ResultDto(
                0,
                0,
                0,
                DisciplineDto("", UnitDto("", 0))
        )
    }
}


