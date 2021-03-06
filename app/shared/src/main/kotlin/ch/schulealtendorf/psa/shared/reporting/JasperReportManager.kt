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

package ch.schulealtendorf.psa.shared.reporting

import ch.schulealtendorf.psa.core.io.AppDirectory
import ch.schulealtendorf.psa.core.io.ApplicationFile
import ch.schulealtendorf.psa.core.io.FileSystem
import mu.KotlinLogging
import net.sf.jasperreports.engine.JREmptyDataSource
import net.sf.jasperreports.engine.JasperCompileManager
import net.sf.jasperreports.engine.JasperExportManager
import net.sf.jasperreports.engine.JasperFillManager
import net.sf.jasperreports.engine.JasperPrint
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

/**
 * Report manager with jasper reports.
 *
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.1.0
 */
@Component
class JasperReportManager(
    private val filesystem: FileSystem
) : ReportManager {
    private val logoPath = "${filesystem.applicationDir}/reporting/gemeinde-altendorf.jpg"
    private val log = KotlinLogging.logger {}

    override fun exportToPdf(template: Template): InputStream {
        if (File(logoPath).exists().not()) {
            copyResources()
        }

        val report = JasperCompileManager.compileReport(template.source)
        val parameters = template.parameters.plus("logoPath" to logoPath)

        val jasperPrint: JasperPrint = JasperFillManager.fillReport(report, parameters, JREmptyDataSource())
        val output = JasperExportManager.exportReportToPdf(jasperPrint)

        return ByteArrayInputStream(output)
    }

    private fun copyResources() {
        log.debug { "Copy jasper resources to application directory ${filesystem.applicationDir}" }
        val altendorfLogo = JasperReportManager::class.java.getResourceAsStream("/img/gemeinde-altendorf.jpg")
        val applicationFile = ApplicationFile(AppDirectory.REPORTING, "gemeinde-altendorf.jpg")
        filesystem.write(applicationFile, altendorfLogo)
    }
}
