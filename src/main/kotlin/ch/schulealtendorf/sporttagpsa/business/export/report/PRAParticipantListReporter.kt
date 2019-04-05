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

package ch.schulealtendorf.sporttagpsa.business.export.report

import ch.schulealtendorf.pra.api.ParticipantListAPI
import ch.schulealtendorf.pra.api.ReportAPIException
import ch.schulealtendorf.pra.pojo.Participant
import ch.schulealtendorf.pra.pojo.ParticipantList
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.model.Sport
import ch.schulealtendorf.sporttagpsa.repository.AbsentParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.ParticipantRepository
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException

/**
 * Participant list reporter that uses PRA.
 * https://github.com/BilledTrain380/PRA
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PRAParticipantListReporter(
        private val fileSystem: FileSystem,
        private val participantRepository: ParticipantRepository,
        private val participantListAPI: ParticipantListAPI,
        private val absentCompetitorRepository: AbsentParticipantRepository
): ParticipantListReporter {

    /**
     * Generates reports depending on the given {@code data}.
     *
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: Iterable<Sport>): Set<File> {

        try {
            val absentCompetitorList = absentCompetitorRepository.findAll()

            return data
                    .map {

                        val participants = participantRepository.findBySportName(it.name)

                        val participantList = ParticipantList().apply {
                            sport = it.name
                            this.participants = participants
                                    .filter { !absentCompetitorList.any { absent -> absent.participant.id == it.id } }
                                    .map {
                                        Participant().apply {
                                            prename = it.prename
                                            surname = it.surname
                                            isGender = it.gender.asBoolean()
                                            clazz = it.group.name
                                            teacher = it.group.coach.name
                                        }
                                    }
                        }

                        val report = participantListAPI.createReport(participantList)
                        val file = ApplicationFile("export", "participant-list", "Teilnehmerliste ${it.name}.pdf")
                        fileSystem.write(file, report)

                    }.toSet()

        } catch (ex: IOException) {
            throw ReportGenerationException("Could not generate participant list: cause=${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not generate participant list: cause=${ex.message}", ex)
        }
    }

    private fun Gender.asBoolean() = this == Gender.MALE
}