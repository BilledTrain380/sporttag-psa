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

package ch.schulealtendorf.sporttagpsa.controller.participant.importpage

import ch.schulealtendorf.sporttagpsa.business.competitors.CompetitorListConsumer
import ch.schulealtendorf.sporttagpsa.business.parsing.FileReader
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes

/**
 * Controller to upload a multipart file with the competitors data.
 * 
 * @author nmaerchy
 * @version 0.0.1
 */
@Controller
@RequestMapping("/participant/import")
class ImportController(
        private val fileReader: FileReader,
        private val competitorConsumer: CompetitorListConsumer
) {
    
    @GetMapping
    fun import(): String {
        return "participant/import/import"
    }

    /**
     * Handles the passed in MultipartFile.
     * The Multipart file MUST be a text/csv mime type.
     * A success message will be set on the RedirectAttributes if no validation error occurs,
     * otherwise a error message will be set.
     * 
     * @param file a csv file to upload
     * @param redirectAttributes holder for redirect attributes
     * 
     * @return a thymeleaf template
     */
    @PostMapping
    fun handleFileUpload(@RequestParam("competitor-input") file: MultipartFile, redirectAttributes: RedirectAttributes): String {

        return try {

            competitorConsumer.accept(fileReader.parseToCompetitor(file))

            redirectAttributes.addFlashAttribute("success", true)

            "redirect:/participant/import"

        } catch (ex: IllegalArgumentException) {

            redirectAttributes.addFlashAttribute("success", false)

            "redirect:/participant/import"
        }
    }
}