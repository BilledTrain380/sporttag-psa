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

package ch.schulealtendorf.sporttagpsa.business.export

import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.sporttagpsa.business.export.report.DisciplineGroupRankingReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.DisciplineRankingReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.EventSheetReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.ParticipantListReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.TotalRankingReporter
import org.springframework.stereotype.Component
import java.io.File
import java.util.*

/**
 * Export manager that uses PRA.
 * https://github.com/BilledTrain380/PRA
 *
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class ExportManagerImpl(
        private val fileSystem: FileSystem,
        private val eventSheetReporter: EventSheetReporter,
        private val participantListReporter: ParticipantListReporter,
        private val totalRankingReporter: TotalRankingReporter,
        private val disciplineGroupRankingReporter: DisciplineGroupRankingReporter,
        private val disciplineRankingReporter: DisciplineRankingReporter
) : ExportManager {

    private val resourceBundle = ResourceBundle.getBundle("i18n.archives")

    /**
     * Generates an archive file for the event sheets by the given {@code data}.
     *
     * @param data contains the data to generate an archive
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    override fun generateArchive(data: EventSheetExport): File {
        try {

            val reports = eventSheetReporter.generateReport(data.disciplines)

            val file = ApplicationFile("export", resourceBundle.getString("name.event-sheets"))
            return fileSystem.createArchive(file, reports)

        } catch (ex: Exception) {
            throw ArchiveGenerationException("Could not generate archive: case=${ex.message}", ex)
        }
    }

    /**
     * Generates an archive file for the rankings by the given {@code data}.
     *
     * @param data contains the data to generate the archive
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    override fun generateArchive(data: RankingExport): File {
        try {

            val reports = setOf(
                    totalRankingReporter.generateReport(data.total),
                    disciplineGroupRankingReporter.generateReport(data.disciplineGroup),
                    disciplineRankingReporter.generateReport(data.disciplines),
                    disciplineGroupRankingReporter.generateCSV(data.ubsCup)
            ).flatten()

            val file = ApplicationFile("export", resourceBundle.getString("name.ranking"))
            return fileSystem.createArchive(file, reports)

        } catch (ex: Exception) {
            throw ArchiveGenerationException("Could not generate archive: case=${ex.message}", ex)
        }
    }

    /**
     * Generates an archive file for the participant list by the given {@code data}.
     *
     * @param data contains the data to generate the archive
     *
     * @return the generated archive
     * @throws ArchiveGenerationException if the archive could not be generated
     */
    override fun generateArchive(data: ParticipantExport): File {
        try {

            val reports = participantListReporter.generateReport(data.sports)

            val file = ApplicationFile("export", resourceBundle.getString("name.participant-list"))
            return fileSystem.createArchive(file, reports)

        } catch (ex: Exception) {
            throw ArchiveGenerationException("Could not generate archive: case=${ex.message}", ex)
        }
    }
}