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

import ch.schulealtendorf.pra.api.DisciplineGroupRankingAPI
import ch.schulealtendorf.pra.api.ReportAPIException
import ch.schulealtendorf.pra.pojo.Discipline
import ch.schulealtendorf.pra.pojo.DisciplineGroupCompetitor
import ch.schulealtendorf.pra.pojo.DisciplineGroupRanking
import ch.schulealtendorf.pra.pojo.Result
import ch.schulealtendorf.sporttagpsa.filesystem.FileSystem
import ch.schulealtendorf.sporttagpsa.repository.StarterRepository
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.time.Year

/**
 * Discipline group ranking reporter that uses PRA.
 * https://github.com/BilledTrain380/PRA
 *
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PRADisciplineGroupRankingReporter(
        private val fileSystem: FileSystem,
        private val starterRepository: StarterRepository,
        private val disciplineGroupRankingAPI: DisciplineGroupRankingAPI
): DisciplineGroupRankingReporter {

    /**
     * Generates reports depending on the given {@code data}.
     *
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: Iterable<Boolean>): Set<File> {

        try {
            return data.map { gender ->
    
                starterRepository.findByCompetitorGender(gender)
                    .groupBy { DateTime(it.competitor.birthday).year }
                    .map {
    
                        val ranking = DisciplineGroupRanking().apply {
                            year = Year.of(it.key)
                            isGender = gender
                            this.competitors = it.value.map {
                                DisciplineGroupCompetitor().apply {
                                    prename = it.competitor.prename
                                    surname = it.competitor.surname
                                    clazz = it.competitor.clazz.name
    
                                    ballwurf = Discipline().apply {
                                        val resultEntity = it.results.single { it.discipline.name == "Ballwurf" }
    
                                        setDistance(resultEntity.distance)
                                        result = Result(resultEntity.result)
                                        points = resultEntity.points
                                    }
    
                                    weitsprung = Discipline().apply {
                                        val resultEntity = it.results.single { it.discipline.name == "Weitsprung" }
    
                                        setDistance(resultEntity.distance)
                                        result = Result(resultEntity.result)
                                        points = resultEntity.points
                                    }
    
                                    schnelllauf = Discipline().apply {
                                        val resultEntity = it.results.single { it.discipline.name == "Schnelllauf" }
    
                                        setDistance(resultEntity.distance)
                                        result = Result(resultEntity.result)
                                        points = resultEntity.points
                                    }
                                }
                            }
                        }
    
                        val report = disciplineGroupRankingAPI.createReport(ranking)
    
                        fileSystem.write("Rangliste ${gender.text()} 3-Kampf ${it.key}.pdf", report)
                    }.toSet()
                
            }.flatten().toSet()
            
        } catch (ex: IOException) {
            throw ReportGenerationException("Could not create discipline group report: cause=${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not create discipline group report: cause=${ex.message}", ex)
        }
    }

    private fun Boolean.text() = if(this) "Knaben" else "Mädchen"
}