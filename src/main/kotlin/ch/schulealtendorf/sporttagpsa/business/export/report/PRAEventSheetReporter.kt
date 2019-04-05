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

import ch.schulealtendorf.pra.api.EventSheetAPI
import ch.schulealtendorf.pra.api.ReportAPIException
import ch.schulealtendorf.pra.pojo.Competitor
import ch.schulealtendorf.pra.pojo.EventSheet
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.sporttagpsa.business.export.EventSheetDisciplineExport
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException

/**
 * Event sheet reporter that uses PRA.
 * https://github.com/BilledTrain380/PRA
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PRAEventSheetReporter(
        private val competitorRepository: CompetitorRepository,
        private val fileSystem: FileSystem,
        private val eventSheetAPI: EventSheetAPI
): EventSheetReporter {

    /**
     * Generates a report for each given {@link EventSheetExport}.
     * The {@link StorageManager} determines where the reports are being saved.
     *
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: Iterable<EventSheetDisciplineExport>): Set<File> {

        try {
            return data.map { eventSheetData ->

                val competitorList = competitorRepository.findByParticipantGenderAndParticipantGroupName(eventSheetData.gender, eventSheetData.group.name)

                val eventSheet = EventSheet().apply {
                    clazz = eventSheetData.group.name
                    discipline = eventSheetData.discipline.name
                    isGender = eventSheetData.gender.asBoolean()
                    competitors = competitorList.map {
                        Competitor().apply {
                            startnumber = it.startnumber!!
                            prename = it.participant.prename
                            surname = it.participant.surname
                            it.results
                                    .find { it.discipline.name == eventSheetData.discipline.name }
                                    ?.let {
                                        setDistance(it.distance)
                                    }
                        }
                    }
                }

                val report = eventSheetAPI.createReport(eventSheet)
                val file = ApplicationFile("export", "event-sheets", "Wettkampfblatt ${eventSheetData.discipline.name} ${eventSheetData.group.name} ${eventSheetData.gender.text()}.pdf")
                fileSystem.write(file, report)
            }.toSet()
        } catch (ex: IOException) {
            throw ReportGenerationException("Could not generate event sheets: message=${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not generate event sheets: message=${ex.message}", ex)
        }
    }
    
    private fun Gender.text() = if(this.asBoolean()) "Knaben" else "Mädchen"

    private fun Gender.asBoolean() = this == Gender.MALE
}