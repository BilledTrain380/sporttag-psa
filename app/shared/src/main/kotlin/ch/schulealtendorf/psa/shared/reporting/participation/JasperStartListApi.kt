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

package ch.schulealtendorf.psa.shared.reporting.participation

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.psa.dto.participation.CompetitorDto
import ch.schulealtendorf.psa.shared.reporting.ReportManager
import ch.schulealtendorf.psa.shared.reporting.Template
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource
import org.springframework.stereotype.Component
import java.io.File
import java.io.InputStream
import java.util.ResourceBundle

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class JasperStartListApi(
    private val reportManager: ReportManager,
    private val filesystem: FileSystem
) : StartListApi {
    private val resourceBundle = ResourceBundle.getBundle("i18n.reporting")

    override fun createReport(data: Collection<CompetitorDto>): File {
        val competitors = data
            .filterNot { it.isAbsent }
            .map { StartListDataSet.fromCompetitor(it) }
            .sortedBy { it.startnumber }

        val template = object : Template {
            override val source: InputStream
                get() = JasperParticipantListApi::class.java.getResourceAsStream("/reporting/jasper-templates/startlist.jrxml")
            override val parameters = hashMapOf(
                "competitors" to JRBeanCollectionDataSource(competitors)
            )
        }

        val reportInputStream = reportManager.exportToPdf(template)
        val file = ApplicationFile(AppDirectory.REPORTING, "${resourceBundle.getString("file.name.startlist")}.pdf")

        return filesystem.write(file, reportInputStream)
    }

    override fun createPdfReport(data: Collection<CompetitorDto>, config: Void): File {
        return createReport(data)
    }
}
