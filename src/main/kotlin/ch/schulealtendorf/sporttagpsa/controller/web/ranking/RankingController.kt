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

package ch.schulealtendorf.sporttagpsa.controller.web.ranking

import ch.schulealtendorf.sporttagpsa.business.athletics.DisciplineManager
import ch.schulealtendorf.sporttagpsa.business.export.DisciplineExport
import ch.schulealtendorf.sporttagpsa.business.export.ExportManager
import ch.schulealtendorf.sporttagpsa.business.export.RankingExport
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import ch.schulealtendorf.sporttagpsa.controller.web.files.FileQualifier
import ch.schulealtendorf.sporttagpsa.filesystem.FileSystem
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api/web")
class RankingController(
        private val exportManager: ExportManager,
        private val disciplineManager: DisciplineManager,
        private val fileSystem: FileSystem
) {

    @PostMapping("/ranking", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun createRanking(@RequestBody data: RankingData): FileQualifier {


        val disciplineExports = data.discipline.map {

            val discipline = disciplineManager.getDiscipline(it.discipline)
                    .orElseThrow { BadRequestException("The given discipline does not exist: name=${it.discipline}") }

            it.genders.map { DisciplineExport(discipline, it) }
        }.flatten()

        val rankingExport = RankingExport(
            disciplineExports,
                data.disciplineGroup,
                data.total,
                data.ubsCup
        )

        val zip = exportManager.generateArchive(rankingExport)

        return FileQualifier(
                zip.absolutePath.removePrefix(fileSystem.getApplicationDir().absolutePath)
        )
    }
}