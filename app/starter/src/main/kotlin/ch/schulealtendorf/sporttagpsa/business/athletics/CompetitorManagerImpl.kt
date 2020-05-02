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

import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.athletics.ResultDto
import ch.schulealtendorf.psa.shared.rulebook.FormulaModel
import ch.schulealtendorf.psa.shared.rulebook.ResultRuleBook
import ch.schulealtendorf.sporttagpsa.entity.CompetitorEntity
import ch.schulealtendorf.sporttagpsa.lib.competitorDtoOf
import ch.schulealtendorf.sporttagpsa.lib.resultDtoOf
import ch.schulealtendorf.sporttagpsa.lib.toOptional
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.springframework.stereotype.Component
import java.util.Optional

/**
 * {@link CompetitorManager} implementation which uses the repository classes.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Component
class CompetitorManagerImpl(
    private val resultRuleBook: ResultRuleBook,
    private val competitorRepository: CompetitorRepository
) : CompetitorManager {
    override fun getCompetitors(): List<CompetitorDto> {
        return competitorRepository.findAll().map { competitorDtoOf(it) }
    }

    override fun getCompetitors(filter: CompetitorFilter): List<CompetitorDto> {

        // Mainly filter the group on the database query if present
        // We should not have any performance issues by filtering gender and absent in memory
        val competitors: List<CompetitorEntity> = if (filter.group != null) {
            competitorRepository.findByParticipantGroupName(filter.group)
        } else {
            competitorRepository.findAll().toList()
        }

        return competitors
            .filter { filter.filterByGender(it.participant.gender) }
            .filter { filter.filterByAbsent(it.participant.absent) }
            .map { competitorDtoOf(it) }
    }

    override fun getCompetitor(id: Int): Optional<CompetitorDto> {
        return competitorRepository.findByParticipantId(id).map { competitorDtoOf(it) }
    }

    override fun updateResult(resultAmend: CompetitorResultAmend): ResultDto {
        val competitor = competitorRepository.findByParticipantId(resultAmend.competitorId)
            .orElseThrow { NoSuchElementException("Could not find competitor: id=${resultAmend.competitorId}") }

        val result = competitor.results
            .find { it.id == resultAmend.result.id }
            .toOptional()
            .orElseThrow { NoSuchElementException("Could not find result: id=${resultAmend.result.id}") }

        val formulaModel = FormulaModel(
            discipline = result.discipline.name,
            distance = result.distance,
            result = resultAmend.result.value.toDouble() / result.discipline.unit.factor,
            gender = result.competitor.participant.gender
        )

        result.apply {
            value = resultAmend.result.value
            points = resultRuleBook.calc(formulaModel)
        }

        competitorRepository.save(competitor)

        return resultDtoOf(result)
    }

    override fun deleteCompetitor(startNumber: Int) {
        competitorRepository.deleteById(startNumber)
    }
}
