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

import ch.schulealtendorf.psa.dto.participation.SportDto
import ch.schulealtendorf.psa.service.standard.export.ReportGenerationException
import ch.schulealtendorf.psa.service.standard.participantDtoOf
import ch.schulealtendorf.psa.service.standard.repository.ParticipantRepository
import ch.schulealtendorf.psa.shared.reporting.participation.ParticipantListApi
import org.springframework.stereotype.Component
import java.io.File

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class ParticipantListReporterImpl(
    private val participantRepository: ParticipantRepository,
    private val participantListApi: ParticipantListApi
) : ParticipantListReporter {
    override fun generateReport(data: Iterable<SportDto>): Set<File> {
        return try {
            data.map { sport ->
                val participants = participantRepository
                    .findBySportName(sport.name)
                    .map { participantDtoOf(it) }

                participantListApi.createPdfReport(participants, sport)
            }.toSet()
        } catch (exception: Exception) {
            throw ReportGenerationException("Could not generate participant list report", exception)
        }
    }
}
