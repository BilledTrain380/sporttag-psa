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

import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.ResultDto
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.from
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import ch.schulealtendorf.sporttagpsa.repository.DisciplineRepository
import org.springframework.stereotype.Component
import java.util.*
import kotlin.NoSuchElementException

/**
 * {@link CompetitorManager} implementation which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class CompetitorManagerImpl(
        private val competitorRepository: CompetitorRepository,
        private val disciplineRepository: DisciplineRepository
) : CompetitorManager {

    /**
     * @return a list of all competitors
     */
    override fun getCompetitorList() = competitorRepository.findAll().map { CompetitorDto from it }

    /**
     * Get a competitor as a {@link Optional} matching the given {@code id}.
     *
     * If no competitor can be found an empty Optional will be returned.
     *
     * @param id the ID of the competitor
     *
     * @return an Optional containing the resulting competitor
     */
    override fun getCompetitor(id: Int): Optional<CompetitorDto> = competitorRepository.findByParticipantId(id).map { CompetitorDto from it }

    override fun saveCompetitorResults(competitor: CompetitorDto) {

        val competitorEntity = competitorRepository.findByParticipantId(competitor.id)
                .orElseThrow { NoSuchElementException("Could not find competitor: id=${competitor.id}") }

        val results = competitor.results
                .map {

                    competitorEntity.results.findById(it.id).orElseGet {

                        val discipline = disciplineRepository.findById(it.discipline.name)
                                .orElseThrow { NoSuchElementException("Could not find discipline: name=${it.discipline.name}") }

                        ResultEntity(
                                distance = it.distance,
                                discipline = discipline
                        )
                    }.apply {
                        value = it.value
                        points = it.points
                    }
                }.toSet()

        competitorEntity.results = results
        competitorRepository.save(competitorEntity)
    }

    override fun mergeResults(competitor: CompetitorDto, results: Iterable<ResultDto>): CompetitorDto {

        val mergedResults = competitor.results
                .map {
                    if (it.existsIn(results)) results take it else it
                }.plus(results.notIn(competitor.results))


        return competitor.copy(results = mergedResults)
    }

    private fun Set<ResultEntity>.findById(id: Int): Optional<ResultEntity> {
        return Optional.ofNullable(find { it.id == id })
    }

    private fun ResultDto.existsIn(results: Iterable<ResultDto>): Boolean {
        return results.any { id == it.id }
    }

    private infix fun Iterable<ResultDto>.take(result: ResultDto): ResultDto {
        return find { it.id == result.id }!!
    }

    private fun Iterable<ResultDto>.notIn(results: Iterable<ResultDto>): Iterable<ResultDto> {
        return filterNot { results.any { result -> result.id == it.id } }
    }
}