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
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.sporttagpsa.model.Gender
import ch.schulealtendorf.sporttagpsa.repository.AbsentParticipantRepository
import ch.schulealtendorf.sporttagpsa.repository.CompetitorRepository
import org.joda.time.DateTime
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Year
import java.util.*

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
        private val competitorRepository: CompetitorRepository,
        private val disciplineGroupRankingAPI: DisciplineGroupRankingAPI,
        private val absentParticipantRepository: AbsentParticipantRepository
) : DisciplineGroupRankingReporter {

    /**
     * Generates reports depending on the given {@code data}.
     *
     * @param data the data for the report/s
     *
     * @return all generated reports
     * @throws ReportGenerationException if the report generation fails
     */
    override fun generateReport(data: Iterable<Gender>): Set<File> {

        try {

            val absentParticipantList = absentParticipantRepository.findAll()

            return data.map { gender ->

                competitorRepository.findByParticipantGender(gender)
                        .filterNot { absentParticipantList.any { absentParticipant -> absentParticipant.participant.id == it.participant.id } }
                        .groupBy { DateTime(it.participant.birthday).year }
                        .map {

                            val ranking = DisciplineGroupRanking().apply {
                                year = Year.of(it.key)
                                isGender = gender.asBoolean()
                                competitors = it.value.map {
                                    DisciplineGroupCompetitor().apply {
                                        prename = it.participant.prename
                                        surname = it.participant.surname
                                        clazz = it.participant.group.name

                                        ballwurf = Discipline().apply {
                                            val resultEntity = it.results.single { it.discipline.name == "Ballwurf" }

                                            setDistance(resultEntity.distance)
                                            result = Result(resultEntity.value.toDouble() / resultEntity.discipline.unit.factor)
                                            points = resultEntity.points
                                        }

                                        weitsprung = Discipline().apply {
                                            val resultEntity = it.results.single { it.discipline.name == "Weitsprung" }

                                            setDistance(resultEntity.distance)
                                            result = Result(resultEntity.value.toDouble() / resultEntity.discipline.unit.factor)
                                            points = resultEntity.points
                                        }

                                        schnelllauf = Discipline().apply {
                                            val resultEntity = it.results.single { it.discipline.name == "Schnelllauf" }

                                            setDistance(resultEntity.distance)
                                            result = Result(resultEntity.value.toDouble() / resultEntity.discipline.unit.factor)
                                            points = resultEntity.points
                                        }
                                    }
                                }
                            }

                            val report = disciplineGroupRankingAPI.createReport(ranking)

                            val file = ApplicationFile("export", "ranking", "Rangliste ${gender.text()} 3-Kampf ${it.key}.pdf")
                            fileSystem.write(file, report)
                        }.toSet()

            }.flatten().toSet()

        } catch (ex: IOException) {
            throw ReportGenerationException("Could not create discipline group report: cause=${ex.message}", ex)
        } catch (ex: ReportAPIException) {
            throw ReportGenerationException("Could not create discipline group report: cause=${ex.message}", ex)
        }
    }

    /**
     * Generates csv file of discipline group ranking for all given {@code genders}.
     *
     * The generated files are sorted by gender and age.
     *
     * @param genders all genders that should be included in the csv
     *
     * @return all generated csv files
     * @throws ReportGenerationException if the csv files could not be generated
     */
    override fun generateCSV(genders: Iterable<Gender>): Set<File> {

        try {

            val absentCompetitorList = absentParticipantRepository.findAll()

            return genders.map { gender ->

                competitorRepository.findByParticipantGender(gender)
                        .filterNot { absentCompetitorList.any { absentParticipant -> absentParticipant.participant.id == it.participant.id } }
                        .groupBy { DateTime(it.participant.birthday).year }
                        .map {

                            val headers = "Startnummer,Name,Vorname,Adresse,PLZ,Ort,Geburtsdatum,Verein / Schule,Schnelllauf,Weitsprung,Ballwurf"

                            val lines = it.value
                                    .reversed()
                                    .map {

                                        val ballwurf = Result(it.results.single { it.discipline.name == "Ballwurf" }.let { it.value.toDouble() / it.discipline.unit.factor })
                                        val schnelllauf = Result(it.results.single { it.discipline.name == "Schnelllauf" }.let { it.value.toDouble() / it.discipline.unit.factor })
                                        val weitsprung = Result(it.results.single { it.discipline.name == "Weitsprung" }.let { it.value.toDouble() / it.discipline.unit.factor })

                                        listOf(
                                                it.startnumber.toString(),
                                                it.participant.surname,
                                                it.participant.prename,
                                                it.participant.address,
                                                it.participant.town.zip,
                                                it.participant.town.name,
                                                it.participant.birthday.formattedDate(),
                                                "Primarschule Altendorf / KTV",
                                                schnelllauf.toString(),
                                                weitsprung.toString(),
                                                ballwurf.toString()
                                        ).joinToString(",") { it }
                                    }.plus(headers).reversed()

                            val file = ApplicationFile("export", "ranking", "UBS - ${gender.text()} - ${it.key}.csv")
                            fileSystem.write(file, lines)
                        }.toSet()

            }.flatten().toSet()

        } catch (ex: IOException) {
            throw ReportGenerationException("Could not create UBS Cup ranking: cause=${ex.message}", ex)
        }
    }

    private fun Gender.asBoolean() = this == Gender.MALE

    private fun Gender.text() = if (this.asBoolean()) "Knaben" else "Mädchen"

    private fun Long.formattedDate(): String {
        val date = Date(this)
        val formatter = SimpleDateFormat("dd.MM.yyyy")

        return formatter.format(date)
    }
}