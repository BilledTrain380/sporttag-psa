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

package ch.schulealtendorf.psa.shared.reporting.ranking

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.dto.participation.athletics.BALLWURF
import ch.schulealtendorf.psa.dto.participation.athletics.SCHNELLLAUF
import ch.schulealtendorf.psa.dto.participation.athletics.WEITSPRUNG
import ch.schulealtendorf.psa.shared.reporting.ReportManager
import ch.schulealtendorf.psa.shared.reporting.Template
import ch.schulealtendorf.psa.shared.reporting.csvNameOf
import ch.schulealtendorf.psa.shared.reporting.pdfNameOf
import mu.KotlinLogging
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.time.Year
import java.util.ResourceBundle

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class JasperDisciplineGroupApi(
    private val reportManager: ReportManager,
    private val rankingManager: RankingManager,
    private val filesystem: FileSystem
) : DisciplineGroupRankingApi {
    private val resourceBundle = ResourceBundle.getBundle("i18n.reporting")
    private val log = KotlinLogging.logger {}

    override fun createCsvReport(data: Collection<CompetitorDto>, config: DisciplineGroupConfig): File {
        log.info { "Create csv triathlon ranking report: $config" }

        val competitors = data filterByConfig config

        val lines = listOf(resourceBundle.getString("ranking.csv.header-line"))
            .plus(
                competitors.map { competitor ->
                    listOf(
                        competitor.startnumber.toString(),
                        competitor.surname,
                        competitor.prename,
                        competitor.address,
                        competitor.town.zip,
                        competitor.town.name,
                        competitor.birthday.format(),
                        "Primarschule Altendorf / KTV",
                        competitor.findResultByDiscipline(SCHNELLLAUF)
                            .map { it.relativeValue }
                            .orElse(""),
                        competitor.findResultByDiscipline(WEITSPRUNG)
                            .map { it.relativeValue }
                            .orElse(""),
                        competitor.findResultByDiscipline(BALLWURF)
                            .map { it.relativeValue }
                            .orElse("")
                    ).joinToString(",") { it }
                }
            )

        val file = ApplicationFile(AppDirectory.REPORTING, csvNameOf(config))

        return filesystem.write(file, lines)
    }

    override fun createPdfReport(data: Collection<CompetitorDto>, config: DisciplineGroupConfig): File {
        log.info { "Create pdf triathlon report: $config" }

        val competitors = data filterByConfig config
        val rankingDataSet = rankingManager.createDisciplineGroupRanking(competitors)

        val template = object : Template {
            override val source: InputStream =
                JasperDisciplineGroupApi::class.java.getResourceAsStream("/reporting/jasper-templates/discipline-group-ranking.jrxml")
            override val parameters: Map<String, Any> = hashMapOf(
                "gender" to config.gender.text,
                "year" to config.year.value,
                "age" to Year.now().value - config.year.value,
                "competitors" to JRBeanCollectionDataSource(rankingDataSet)
            )
        }

        val reportInputStream = reportManager.exportToPdf(template)
        val file = ApplicationFile(AppDirectory.REPORTING, pdfNameOf(config))

        return filesystem.write(file, reportInputStream)
    }

    private infix fun Collection<CompetitorDto>.filterByConfig(config: DisciplineGroupConfig): List<CompetitorDto> {
        return this
            .filter { it.gender == config.gender }
            .filter { it.birthday.year == config.year }
            .filterNot { it.isAbsent }
    }
}
