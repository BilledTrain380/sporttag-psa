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

package ch.schulealtendorf.sporttagpsa.controller.web.app

import ch.schulealtendorf.psa.core.io.FileSystem
import ch.schulealtendorf.sporttagpsa.business.athletics.DisciplineManager
import ch.schulealtendorf.sporttagpsa.business.export.EventSheetDisciplineExport
import ch.schulealtendorf.sporttagpsa.business.export.EventSheetExport
import ch.schulealtendorf.sporttagpsa.business.export.ExportManager
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import org.springframework.http.MediaType
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping("/api")
class EventSheetController(
    private val exportManager: ExportManager,
    private val disciplineManager: DisciplineManager,
    private val groupManager: GroupManager,
    private val fileSystem: FileSystem
) {
    @PreAuthorize("#oauth2.hasScope('event_sheets')")
    @PostMapping(
        "/event-sheets",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseBody
    fun createEventSheets(@RequestBody data: List<EventSheetData>): FileQualifier {
        val exports = data.map {
            val discipline = disciplineManager.getDiscipline(it.discipline)
                .orElseThrow { BadRequestException("The discipline does not exist: name=${it.discipline}") }

            val group = groupManager.getGroup(it.group)
                .orElseThrow { BadRequestException("The group does not exist: name=${it.group}") }

            EventSheetDisciplineExport(discipline, group, it.gender)
        }

        val exportData = EventSheetExport(exports)

        val zip = exportManager.generateArchive(exportData)
        return FileQualifier.ofPath(zip.absolutePath.removePrefix(fileSystem.getApplicationDir().absolutePath))
    }
}
