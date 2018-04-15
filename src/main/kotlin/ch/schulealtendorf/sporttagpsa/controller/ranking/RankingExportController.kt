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

package ch.schulealtendorf.sporttagpsa.controller.ranking

import ch.schulealtendorf.sporttagpsa.business.export.DisciplineExport
import ch.schulealtendorf.sporttagpsa.business.export.ExportManager
import ch.schulealtendorf.sporttagpsa.business.export.RankingExport
import ch.schulealtendorf.sporttagpsa.business.export.SimpleDiscipline
import ch.schulealtendorf.sporttagpsa.business.provider.DisciplineProvider
import ch.schulealtendorf.sporttagpsa.model.Gender
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import java.io.FileInputStream
import javax.validation.Valid

@Controller
@RequestMapping("/ranking")
class RankingExportController(
        private val exportManager: ExportManager,
        disciplineProvider: DisciplineProvider
) {

    private val disciplines = disciplineProvider.getAll()
    
    @GetMapping
    fun index(model: Model): String {
        
        val rankingForm = RankingForm(
                disciplines.map { 
                    DisciplineRankingForm(it.id, it.name)
                }
        )
        
        model.addAttribute("rankingForm", rankingForm)
        
        return "ranking/ranking"
    }
    
    @PostMapping("/export")
    fun export(@Valid @ModelAttribute("rankingForm") rankingForm: RankingForm): ResponseEntity<InputStreamResource> {
        
        val zip = exportManager.generateArchive(rankingForm.toRankingExport())

        val respHeaders = HttpHeaders()
        respHeaders.contentType = MediaType.APPLICATION_OCTET_STREAM
        respHeaders.contentLength = zip.length()
        respHeaders.setContentDispositionFormData("attachment", zip.name)

        val isr = InputStreamResource(FileInputStream(zip))
        return ResponseEntity(isr, respHeaders, HttpStatus.OK)
    }
    
    private fun RankingForm.toRankingExport(): RankingExport {

        val disciplineExport = listOf(
                disciplines
                        .filter { it.male }
                        .map { DisciplineExport(SimpleDiscipline(it.id, it.name), true) },
                disciplines
                        .filter { it.female }
                        .map { DisciplineExport(SimpleDiscipline(it.id, it.name)) }
        ).flatten()

        val disciplineGroupExport = listOf(
                disciplineGroup.female,
                disciplineGroup.male
        ).filter { it }

        val totalExport = listOf(
                total.female,
                total.male
        ).filter { it }

        val ubsCupExport = listOf(
                Gender(ubsCup.female),
                Gender(ubsCup.male)
        ).filter { it.value }.toSet()

        return RankingExport(
                disciplineExport,
                disciplineGroupExport,
                totalExport,
                ubsCupExport
        )
    }
}