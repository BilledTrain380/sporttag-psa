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

import ch.schulealtendorf.sporttagpsa.business.group.CSVParsingException
import ch.schulealtendorf.sporttagpsa.business.group.GroupFileParser
import ch.schulealtendorf.sporttagpsa.business.group.GroupManager
import ch.schulealtendorf.sporttagpsa.controller.rest.BadRequestException
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile

/**
 * @author nmaerchy <billedtrain380@gmail.com>
 * @since 2.0.0
 */
@Controller
@RequestMapping("/web")
class GroupImportController(
    private val fileParser: GroupFileParser,
    private val groupManager: GroupManager
) {
    @PreAuthorize("#oauth2.hasScope('group_write')")
    @PostMapping("/import-group", consumes = ["multipart/form-data"])
    fun importGroup(@RequestParam("group-input") file: MultipartFile): ResponseEntity<String> {
        try {
            val participants = fileParser.parseCSV(file)
            participants.forEach(groupManager::import)

            return ResponseEntity.ok("Group import successful")
        } catch (exception: CSVParsingException) {
            // we increment the line, so its not zero based line number for the user
            throw BadRequestException("${exception.message} (at line ${exception.line + 1}:${exception.column})")
        } catch (exception: IllegalArgumentException) {
            throw BadRequestException(exception.message)
        }
    }
}
