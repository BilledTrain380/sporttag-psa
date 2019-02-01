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
import ch.schulealtendorf.sporttagpsa.business.export.DisciplineExport
import ch.schulealtendorf.sporttagpsa.entity.ResultEntity
import ch.schulealtendorf.sporttagpsa.filesystem.ApplicationFile
import ch.schulealtendorf.sporttagpsa.filesystem.FileSystem
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.repository.AbsentParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.time.Year

/**
 * Discipline ranking reporter that uses PRA.
 * https://github.com/BilledTrain380/PRA
 * 
 * @author nmaerchy
 * @version 1.0.0
 */
@Component
class PRADisciplineRankingReporter(
        private val fileSystem: FileSystem,
        private val competitorRepository: CompetitorRepository,
        private val disciplineRankingAPI: DisciplineRankingAPI,
        private val absentParticipantRepository: AbsentParticipantRepository
): DisciplineRankingReporter {

    /**
     * Generates reports for all given disciplines.
     * 
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: Iterable<DisciplineExport>): Set<File> {

        try {

            val absentParticipantList = absentParticipantRepository.findAll()

            return data.map { disciplineExport ->

                val competitorList = competitorRepository.findByParticipantGender(disciplineExport.gender)

                competitorList
                        .filterNot { absentParticipantList.any { absentParticipant -> absentParticipant.participant.id == it.participant.id } }
                        .groupBy { DateTime(it.participant.birthday).year }
                        .map {

                            val ranking = DisciplineRanking().apply {
                                year = Year.of(it.key)
                                isGender = disciplineExport.gender.asBoolean()
                                discipline = disciplineExport.discipline.name
                                competitors = it.value.map {
                                    DisciplineCompetitor().apply {
                                        prename = it.participant.prename
                                        surname = it.participant.surname
                                        clazz = it.participant.group.name

                                        it.results
                                                .find { result -> result.discipline.name == disciplineExport.discipline.name }
                                                .let {
                                                    result = it!!.result()
                                                    points = it.points
                                                }
                                    }
                                }
                            }

                            val report = disciplineRankingAPI.createReport(ranking)
                            val file = ApplicationFile("export", "ranking", "Rangliste ${disciplineExport.gender.text()} ${disciplineExport.discipline.name} ${it.key}.pdf")
                            fileSystem.write(file, report)
                        }.toSet()
            }.flatten().toSet()

        } catch (ex: IOException) {
            throw ReportGenerationException("Could not generate discipline ranking: cause=${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not generate discipline ranking: cause=${ex.message}", ex)
        }
    }
    
    private fun Gender.text() = if(this == Gender.MALE) "Knaben" else "Mädchen"

    private fun ResultEntity.result(): Result {

        val value: Double = value.toDouble() / discipline.unit.factor

        if (value % 1 == 0.0) {
            return Result(value.toInt())
        }

        return Result(value)
    }

    private fun Gender.asBoolean() = this == Gender.MALE
}