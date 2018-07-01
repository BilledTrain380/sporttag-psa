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

package ch.schulealtendorf.sporttagpsa.controller.participant.management

import ch.schulealtendorf.sporttagpsa.business.export.ExportManager
import ch.schulealtendorf.sporttagpsa.business.export.ParticipantExport
import ch.schulealtendorf.sporttagpsa.business.export.SimpleSport
import ch.schulealtendorf.sporttagpsa.business.provider.SportProvider
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
@RequestMapping("/participant/management/participant-list")
class ParticipantListController(
        private val exportManager: ExportManager,
        sportProvider: SportProvider
) {
    
    private val sports = sportProvider.getAll().filter { it.name == "Mehrkampf" }
    
    @GetMapping
    fun index(model: Model): String {
        
        val participantListForm = ParticipantListForm(sports.map { ParticipantSport(it.id, it.name) })
        
        model.addAttribute("participantListForm", participantListForm)
        
        return "participant/management/participant-list"
    }

    @PostMapping("/export")
    fun export(@Valid @ModelAttribute("participantListForm") participantListForm: ParticipantListForm): ResponseEntity<InputStreamResource> {

        val zip = exportManager.generateArchive(participantListForm.toParticipantExport())
        
        val respHeaders = HttpHeaders()
        respHeaders.contentType = MediaType.APPLICATION_OCTET_STREAM
        respHeaders.contentLength = zip.length()
        respHeaders.setContentDispositionFormData("attachment", zip.name)

        val isr = InputStreamResource(FileInputStream(zip))
        return ResponseEntity(isr, respHeaders, HttpStatus.OK)
    }
    
    private fun ParticipantListForm.toParticipantExport(): ParticipantExport {
        return ParticipantExport(
                sports
                        .filter { it.include }
                        .map { SimpleSport(it.id, it.name) }
        )
    }
}