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

package ch.schulealtendorf.sporttagpsa.business.export.report

import ch.schulealtendorf.psa.dto.CompetitorDto
import ch.schulealtendorf.psa.dto.GenderDto
import ch.schulealtendorf.psa.shared.reporting.ranking.DisciplineGroupConfig
import ch.schulealtendorf.psa.shared.reporting.ranking.DisciplineGroupRankingApi
import ch.schulealtendorf.sporttagpsa.from
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.springframework.stereotype.Component
import java.io.File

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class DisciplineGroupRankingReporterImpl(
        private val competitorRepository: CompetitorRepository,
        private val disciplineGroupRankingApi: DisciplineGroupRankingApi
) : DisciplineGroupRankingReporter {
    override fun generateCSV(genders: Iterable<GenderDto>): Set<File> {

        return try {
            genders.generateReport(disciplineGroupRankingApi::createCsvReport)
        } catch (exception: Exception) {
            throw ReportGenerationException("Could not generate csv report", exception)
        }
    }

    override fun generateReport(data: Iterable<GenderDto>): Set<File> {

        return try {
            data.generateReport(disciplineGroupRankingApi::createPdfReport)
        } catch (exception: Exception) {
            throw ReportGenerationException("Could not generate pdf report", exception)
        }
    }

    private inline fun Iterable<GenderDto>.generateReport(createReport: (Collection<CompetitorDto>, DisciplineGroupConfig) -> File): Set<File> {

        return this.map { gender ->

            competitorRepository.findByParticipantGender(gender).map { CompetitorDto from it }
                    .groupBy { it.birthday.year }
                    .map { createReport(it.value, DisciplineGroupConfig(gender, it.key)) }
        }.flatten().toSet()
    }
}