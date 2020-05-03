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

import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.athletics.BALLWURF
import ch.schulealtendorf.psa.dto.participation.athletics.BALLZIELWURF
import ch.schulealtendorf.psa.dto.participation.athletics.DisciplineDto
import ch.schulealtendorf.psa.dto.participation.athletics.KORBEINWURF
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.dto.participation.athletics.SEILSPRINGEN
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import org.springframework.stereotype.Component

@Component
internal class PsaRankingManager : RankingManager {
    override fun createDisciplineRanking(
        competitors: Collection<CompetitorDto>,
        discipline: DisciplineDto
    ): List<DisciplineRankingDataSet> {
        var rank = 1
        var previousPoints = -1

        return competitors
            .sortedBy {
                it.findResultByDiscipline(discipline.name)
                    .map { value -> value.points }
                    .orElse(1)
            }
            .reversed() // Highest result first
            .mapIndexed { index, competitor ->
                val result = competitor.findResultByDiscipline(discipline.name)

                // Default values if result is not present
                var value = "0"
                var points = 1

                if (result.isPresent) {
                    value = result.get().relativeValue
                    points = result.get().points
                }

                if (points != previousPoints) {
                    rank = index + 1
                }

                previousPoints = points

                DisciplineRankingDataSet(
                    rank,
                    competitor.prename,
                    competitor.surname,
                    competitor.group.name,
                    value,
                    points
                )
            }
    }

    override fun createDisciplineGroupRanking(competitors: Collection<CompetitorDto>): List<DisciplineGroupRankingDataSet> {
        var rank = 1
        var previousPoints = -1

        return competitors
            .sortedBy { it.results.calculateDisciplineGroupTotal() }
            .reversed() // Highest result first
            .mapIndexed { index, competitor ->
                val totalPoints = competitor.results.calculateDisciplineGroupTotal()

                if (totalPoints != previousPoints) {
                    rank = index + 1
                }

                previousPoints = totalPoints

                val schnelllauf = competitor.findResultByDiscipline(SCHNELLLAUF)
                val ballwurf = competitor.findResultByDiscipline(BALLWURF)
                val weitsprung = competitor.findResultByDiscipline(WEITSPRUNG)

                DisciplineGroupRankingDataSet(
                    rank = rank,
                    prename = competitor.prename,
                    surname = competitor.surname,
                    group = competitor.group.name,
                    total = totalPoints,
                    schnelllaufResult = schnelllauf.map { it.relativeValue }.orElse("0"),
                    schnelllaufPoints = schnelllauf.map { it.points }.orElse(1),
                    weitsprungResult = weitsprung.map { it.relativeValue }.orElse("0"),
                    weitsprungPoints = weitsprung.map { it.points }.orElse(1),
                    ballwurfResult = ballwurf.map { it.relativeValue }.orElse("0"),
                    ballwurfPoints = ballwurf.map { it.points }.orElse(1)
                )
            }
    }

    override fun createTotalRanking(competitors: Collection<CompetitorDto>): List<TotalRankingDataSet> {
        var rank = 1
        var previousPoints = -1

        return competitors
            .sortedBy { it.results.calculateTotal() }
            .reversed() // Highest result first
            .mapIndexed { index, competitor ->
                val totalPoints = competitor.results.calculateTotal()

                if (totalPoints != previousPoints) {
                    rank = index + 1
                }

                previousPoints = totalPoints

                val schnelllauf = competitor.findResultByDiscipline(SCHNELLLAUF)
                val ballwurf = competitor.findResultByDiscipline(BALLWURF)
                val ballzielwurf = competitor.findResultByDiscipline(BALLZIELWURF)
                val korbeinwurf = competitor.findResultByDiscipline(KORBEINWURF)
                val seilspringen = competitor.findResultByDiscipline(SEILSPRINGEN)
                val weitsprung = competitor.findResultByDiscipline(WEITSPRUNG)

                TotalRankingDataSet(
                    rank = rank,
                    prename = competitor.prename,
                    surname = competitor.surname,
                    group = competitor.group.name,
                    total = totalPoints,
                    deletedResult = competitor.results.lowest(),
                    schnelllaufResult = schnelllauf.map { it.relativeValue }.orElse("0"),
                    schnelllaufPoints = schnelllauf.map { it.points }.orElse(1),
                    ballwurfResult = ballwurf.map { it.relativeValue }.orElse("0"),
                    ballwurfPoints = ballwurf.map { it.points }.orElse(1),
                    ballzielWurfResult = ballzielwurf.map { it.relativeValue }.orElse("0"),
                    ballzielWurfPoints = ballzielwurf.map { it.points }.orElse(1),
                    korbeinWurfResult = korbeinwurf.map { it.relativeValue }.orElse("0"),
                    korbeinWurfPoints = korbeinwurf.map { it.points }.orElse(1),
                    seilspringenResult = seilspringen.map { it.relativeValue }.orElse("0"),
                    seilspringenPoints = seilspringen.map { it.points }.orElse(1),
                    weitsprungResult = weitsprung.map { it.relativeValue }.orElse("0"),
                    weitsprungPoints = weitsprung.map { it.points }.orElse(1)
                )
            }
    }

    private fun Map<String, ResultDto>.calculateTotal() =
        this.values.map { it.points }.sorted().reversed().dropLast(1).sum()

    private fun Map<String, ResultDto>.lowest() = this.values.map { it.points }.sorted().reversed().last()

    private fun Map<String, ResultDto>.calculateDisciplineGroupTotal() =
        this.values.filter {
            it.discipline.name == BALLWURF ||
                it.discipline.name == SCHNELLLAUF ||
                it.discipline.name == WEITSPRUNG
        }.map { it.points }.sum()
}
