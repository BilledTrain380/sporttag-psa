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

import ch.schulealtendorf.pra.api.DisciplineRankingAPI
import ch.schulealtendorf.pra.api.ReportAPIException
import ch.schulealtendorf.pra.pojo.DisciplineCompetitor
import ch.schulealtendorf.pra.pojo.DisciplineRanking
import ch.schulealtendorf.pra.pojo.Result
import ch.schulealtendorf.sporttagpsa.business.export.DisciplineRankingExportModel
import ch.schulealtendorf.sporttagpsa.business.storage.StorageManager
import ch.schulealtendorf.sporttagpsa.repository.ResultRepository
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.time.Year

/**
 * Reporter for {@link DisciplineRankingExportModel} which uses PRA report api.
 * https://github.com/BilledTrain380/PRA
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PRADisciplineRankingReporter(
        private val storageManager: StorageManager,
        private val resultRepository: ResultRepository,
        private val disciplineRankingAPI: DisciplineRankingAPI
): DisciplineRankingReporter {

    /**
     * Generates reports depending on the given {@code data}.
     * 
     * Generates reports for a singe discipline.
     * The reports are grouped by the gender and age of the competitors.
     *
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: DisciplineRankingExportModel): Set<File> {
        
        try {

            val reports: MutableSet<File> = mutableSetOf()

            if(data.male) {
                reports.addAll(generateReport(true, data.name))
            }

            if(data.female) {
                reports.addAll(generateReport(false, data.name))
            }

            return reports
            
        } catch (ex: IOException) {
            throw ReportGenerationException("Could not create Report due IOException: ${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not create report due ReportAPIException: ${ex.message}", ex)
        }
    }
    
    private fun generateReport(gender: Boolean, discipline: String): Set<File> {

        val results = resultRepository.findByDisciplineNameAndStarterCompetitorGender(discipline, gender)
        
        return results
                .groupBy { DateTime(it.starter.competitor.birthday).year }
                .map {

                    val ranking = DisciplineRanking().apply {
                        year = Year.of(it.key)
                        isGender = true
                        this.discipline = discipline
                        competitors = it.value.map {
                            DisciplineCompetitor().apply {
                                prename = it.starter.competitor.prename
                                surname = it.starter.competitor.surname
                                clazz = it.starter.competitor.clazz.name
                                result = Result(it.result)
                                points = it.points
                            }
                        }
                    }

                    val report = disciplineRankingAPI.createReport(ranking)

                    storageManager.write("Rangliste ${if(gender) "Knaben" else "Mädchen"} $discipline ${it.key}.pdf", report)
                }.toSet()
    }
}