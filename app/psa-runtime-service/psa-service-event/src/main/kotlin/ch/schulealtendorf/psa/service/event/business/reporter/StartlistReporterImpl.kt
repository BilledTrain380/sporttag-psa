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

package ch.schulealtendorf.psa.service.event.business.reporter

import ch.schulealtendorf.psa.service.standard.competitorDtoOf
import ch.schulealtendorf.psa.service.standard.export.ReportGenerationException
import ch.schulealtendorf.psa.service.standard.repository.CompetitorRepository
import ch.schulealtendorf.psa.shared.reporting.participation.StartListApi
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.io.File

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class StartlistReporterImpl(
    private val competitorRepository: CompetitorRepository,
    private val startListApi: StartListApi
) : StartlistReporter {
    private val log = KotlinLogging.logger {}

    override fun generateReport(data: Void): Set<File> {
        return setOf(generateReport())
    }

    override fun generateReport(): File {
        return try {
            log.info { "Create startlist report" }
            val competitors = competitorRepository.findAll().map { competitorDtoOf(it) }
            startListApi.createReport(competitors)
        } catch (exception: Exception) {
            throw ReportGenerationException("Could not generate start list report", exception)
        }
    }
}
