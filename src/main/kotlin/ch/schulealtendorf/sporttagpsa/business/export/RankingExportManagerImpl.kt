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

import ch.schulealtendorf.sporttagpsa.business.export.archive.ZipManager
import ch.schulealtendorf.sporttagpsa.business.export.report.DisciplineGroupRankingReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.DisciplineRankingReporter
import ch.schulealtendorf.sporttagpsa.business.export.report.TotalRankingReporter
import org.springframework.stereotype.Component
import java.io.File

/**
 * Default export manager implementation.
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class RankingExportManagerImpl(
        private val disciplineRankingReporter: DisciplineRankingReporter,
        private val disciplineGroupRankingReporter: DisciplineGroupRankingReporter,
        private val totalRankingReporter: TotalRankingReporter,
        private val zipManager: ZipManager
): RankingExportManager {

    /**
     * @return a {@link RankingExportModel} which contains discipline data
     */
    override fun getPreparedModel(): RankingExportModel {
        return RankingExportModel(
                listOf(
                        // TODO: use enum
                        DisciplineRankingExportModel("Schnelllauf", false, false),
                        DisciplineRankingExportModel("Ballwurf", false, false),
                        DisciplineRankingExportModel("Ballzielwurf", false, false),
                        DisciplineRankingExportModel("Korbeinwurf", false, false),
                        DisciplineRankingExportModel("Seilspringen", false, false),
                        DisciplineRankingExportModel("Weitsprung", false, false)
                ),
                DisciplineGroupRankingExportModel(false, false),
                TotalRankingExportModel(false, false)
        )
    }

    /**
     * Generates a zip file by the given {@code model}.
     *
     * @param model contains data to generate reports
     *
     * @return the created zip file
     * @throws RankingExportException if the zip could not be created
     */
    override fun generateZip(model: RankingExportModel): File {
        
        try {

            val reports: MutableSet<File> = mutableSetOf()

            model.disciplines.forEach { reports.addAll(disciplineRankingReporter.generateReport(it)) }
            reports.addAll(disciplineGroupRankingReporter.generateReport(model.disciplineGroup))
            reports.addAll(totalRankingReporter.generateReport(model.total))

            return zipManager.createArchive("Ranglisten", reports)
            
        } catch (ex: Exception) {
            throw RankingExportException("Could not create zip: ${ex.message}", ex)
        }
    }
}